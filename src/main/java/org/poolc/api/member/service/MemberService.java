package org.poolc.api.member.service;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.poolc.api.activity.domain.Session;
import org.poolc.api.activity.dto.ActivityResponse;
import org.poolc.api.activity.repository.SessionRepository;
import org.poolc.api.activity.service.ActivityService;
import org.poolc.api.auth.exception.UnauthorizedException;
import org.poolc.api.auth.infra.PasswordHashProvider;
import org.poolc.api.common.domain.YearSemester;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.domain.MemberRole;
import org.poolc.api.member.domain.MemberRoles;
import org.poolc.api.member.dto.MemberResetRequest;
import org.poolc.api.member.dto.MemberResponse;
import org.poolc.api.member.dto.MemberResponseWithHour;
import org.poolc.api.member.dto.MyActivityDetailResponse;
import org.poolc.api.member.dto.MyActivitySummaryResponse;
import org.poolc.api.member.dto.UpdateMemberRequest;
import org.poolc.api.member.exception.DuplicateMemberException;
import org.poolc.api.member.exception.WrongPasswordException;
import org.poolc.api.member.repository.MemberQueryRepository;
import org.poolc.api.member.repository.MemberRepository;
import org.poolc.api.member.vo.MemberCreateValues;
import org.poolc.api.poolc.domain.Poolc;
import org.poolc.api.poolc.service.PoolcService;
import org.poolc.api.project.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordHashProvider passwordHashProvider;
    private final MemberQueryRepository memberQueryRepository;
    private final ActivityService activityService;
    private final SessionRepository sessionRepository;
    private final ProjectRepository projectRepository;
    private final MemberResponseAssembler memberResponseAssembler;
//    private final MailService mailService;
    private final PoolcService poolcService;

    public void create(MemberCreateValues values) {
        boolean hasDuplicate = memberRepository.existsByLoginIDOrEmailOrPhoneNumberOrStudentID(values.getLoginID(),
                values.getEmail(), values.getPhoneNumber(), values.getStudentID());

        if (hasDuplicate) {
            throw new DuplicateMemberException("There are one or more duplicates of the following: loginID, email, phone number or studentID");
        }

        memberRepository.save(
                Member.builder()
                        .UUID(UUID.randomUUID().toString())
                        .loginID(values.getLoginID())
                        .passwordHash(passwordHashProvider.encodePassword(values.getPassword()))
                        .email(values.getEmail())
                        .phoneNumber(values.getPhoneNumber())
                        .name(values.getName())
                        .department(values.getDepartment())
                        .studentID(values.getStudentID())
                        .passwordResetToken(null)
                        .passwordResetTokenValidUntil(null)
                        .profileImageURL(values.getProfileImageURL())
                        .introduction(values.getIntroduction())
                        .isExcepted(false)
                        .roles(MemberRoles.getDefaultFor(MemberRole.UNACCEPTED))
                        .build());
    }

    public void checkMe(Member loginMember) {
        Poolc poolc = poolcService.get();
        if (!poolc.checkSubscriptionPeriod() && !loginMember.isAcceptedMember()) {
            throw new UnauthorizedException("인증받지 않은 회원입니다.");
        }
    }

    public List<MemberResponse> getAllMembersResponse(Member loginMember) {
        List<Member> members = getAllMembers();
        return members.stream()
                .filter(responseMember -> (!Optional.ofNullable(loginMember).isEmpty() && loginMember.isAdmin() || !responseMember.shouldHide()))
                .map(memberResponseAssembler::of)
                .collect(Collectors.toList());
    }

    public List<MemberResponse> getAllMembersResponseByName(String name) {
        return memberRepository.findByName(name)
                .stream()
                .map(memberResponseAssembler::of)
                .collect(Collectors.toList());
    }

    public List<ActivityResponse> getMemberActivityResponses(String loginID) {
        return activityService.findActivitiesByMemberLoginId(loginID)
                .stream().map(ActivityResponse::of)
                .collect(Collectors.toList());
    }

    public List<ActivityResponse> getHostActivityResponses(Member findMember) {
        return activityService.findActivitiesByHost(findMember)
                .stream().map(ActivityResponse::of)
                .collect(Collectors.toList());
    }

    public List<Member> findMembers(List<String> members) {
        return memberRepository.findAllMembersByLoginIDList(members);
    }

    public boolean checkMemberExistsByLoginID(String loginID) {
        return !memberRepository.existsByLoginID(loginID);
    }

    public void checkGetRoles(Member loginMember) {
        Poolc poolc = poolcService.get();
        if (!poolc.checkSubscriptionPeriod() && !loginMember.isAcceptedMember()) {
            throw new UnauthorizedException("인증받지 않은 회원입니다.");
        }
    }

    public void throwIfNotMember(Member member) {
        if(member == null || !member.isMember()) {
            throw new UnauthorizedException("회원이 아닙니다");
        }
    }

    public Member getMemberByLoginID(String loginID) {
        return memberRepository.findByLoginID(loginID)
                .orElseThrow(() -> new NoSuchElementException("No user found with given loginID"));
    }
    public Member getMemberIfRegistered(String loginID, String password) {
        return memberRepository.findByLoginID(loginID)
                .filter(member -> passwordHashProvider.matches(password, member.getPasswordHash()))
                .orElseThrow(() -> new WrongPasswordException("아이디와 비밀번호를 확인해주세요."));
    }
    // TODO: 이부분 좀 더 깨끗하게 Refactoring해야할 거 같다.

    @Transactional
    public List<MemberResponseWithHour> getHoursWithMembers() {
        YearSemester yearSemester = YearSemester.of(LocalDate.now());
        Map<String, BigDecimal> recognizedHoursByLoginId = new HashMap<>();
        List<Member> members = getAllMembersAndUpdateMemberIsExcepted();
        members.forEach(member -> recognizedHoursByLoginId.put(member.getLoginID(), BigDecimal.ZERO));

        List<Session> sessions = sessionRepository.findAllWithActivityAndAttendanceInSemester(
                yearSemester.getFirstDateFromYearSemester(),
                yearSemester.getLastDateFromYearSemester(),
                LocalDate.now()
        );
        for (Session session : sessions) {
            String hostLoginId = session.getActivity().getHost().getLoginID();
            addRecognizedHours(recognizedHoursByLoginId, hostLoginId, getSessionRecognizedHours(session, hostLoginId));

            for (String attendeeLoginId : session.getAttendedMemberLoginIDs()) {
                if (!attendeeLoginId.equals(hostLoginId)) {
                    addRecognizedHours(recognizedHoursByLoginId, attendeeLoginId, getSessionRecognizedHours(session, attendeeLoginId));
                }
            }
        }

        projectRepository.findAll().stream()
                .filter(project -> isInSemester(project.getStartDate(), yearSemester))
                .forEach(project -> project.getMemberLoginIDs()
                        .forEach(loginId -> addRecognizedHours(recognizedHoursByLoginId, loginId, BigDecimal.TEN)));

        return members.stream()
                .map(member -> MemberResponseWithHour.of(member, recognizedHoursByLoginId.get(member.getLoginID())))
                .sorted(Comparator.comparing(response -> response.getMember().getName()))
                .collect(Collectors.toList());
    }

    public Long getMyHour(Member member){
        YearSemester yearSemester = YearSemester.of(LocalDate.now());
        LocalDate startDate = yearSemester.getFirstDateFromYearSemester();
        LocalDate endDate = yearSemester.getLastDateFromYearSemester();
        return memberQueryRepository.getMyHour(member, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public MyActivitySummaryResponse getMyActivitySummary(Member member) {
        YearSemester yearSemester = YearSemester.of(LocalDate.now());
        List<Session> sessions = sessionRepository.findAllWithActivityAndAttendanceInSemester(
                yearSemester.getFirstDateFromYearSemester(),
                yearSemester.getLastDateFromYearSemester(),
                LocalDate.now()
        );

        BigDecimal seminarStudyHours = BigDecimal.ZERO;
        Map<Long, Session> sessionsByActivityId = new HashMap<>();
        Map<Long, BigDecimal> recognizedHoursByActivityId = new HashMap<>();

        for (Session session : sessions) {
            boolean hosted = session.getActivity().getHost().getLoginID().equals(member.getLoginID());
            boolean attended = session.getAttendedMemberLoginIDs().contains(member.getLoginID());

            if (!hosted && !attended) {
                continue;
            }

            BigDecimal recognizedHours = getSessionRecognizedHours(session, member.getLoginID());
            seminarStudyHours = seminarStudyHours.add(recognizedHours);
            Long activityId = session.getActivity().getId();
            sessionsByActivityId.putIfAbsent(activityId, session);
            recognizedHoursByActivityId.merge(activityId, recognizedHours, BigDecimal::add);
        }

        List<MyActivityDetailResponse> seminarStudyActivities = sessionsByActivityId.values().stream()
                .map(session -> MyActivityDetailResponse.builder()
                        .activityId(session.getActivity().getId())
                        .title(session.getActivity().getTitle())
                        .recognizedHours(recognizedHoursByActivityId.get(session.getActivity().getId()))
                        .hosted(session.getActivity().getHost().getLoginID().equals(member.getLoginID()))
                        .build())
                .sorted(Comparator.comparing(MyActivityDetailResponse::getTitle))
                .collect(Collectors.toList());

        List<MyActivityDetailResponse> projectActivities = projectRepository.findProjectsByProjectMembers(member.getLoginID()).stream()
                .filter(project -> isInSemester(project.getStartDate(), yearSemester))
                .map(project -> MyActivityDetailResponse.builder()
                        .activityId(project.getId())
                        .title(project.getName())
                        .recognizedHours(BigDecimal.TEN)
                        .hosted(false)
                        .build())
                .sorted(Comparator.comparing(MyActivityDetailResponse::getTitle))
                .collect(Collectors.toList());
        BigDecimal projectHours = BigDecimal.TEN.multiply(BigDecimal.valueOf(projectActivities.size()));

        return MyActivitySummaryResponse.builder()
                .totalHours(seminarStudyHours.add(projectHours))
                .seminarStudyHours(seminarStudyHours)
                .officialActivityHours(BigDecimal.ZERO)
                .projectHours(projectHours)
                .seminarStudyActivities(seminarStudyActivities)
                .officialActivities(Collections.emptyList())
                .projectActivities(projectActivities)
                .build();
    }

    private boolean isInSemester(LocalDate startDate, YearSemester yearSemester) {
        return startDate != null
                && !startDate.isBefore(yearSemester.getFirstDateFromYearSemester())
                && !startDate.isAfter(yearSemester.getLastDateFromYearSemester());
    }

    private BigDecimal getSessionRecognizedHours(Session session, String loginId) {
        boolean hosted = session.getActivity().getHost().getLoginID().equals(loginId);
        boolean attended = session.getAttendedMemberLoginIDs().contains(loginId);

        if (!hosted && !attended) {
            return BigDecimal.ZERO;
        }

        BigDecimal sessionHours = BigDecimal.valueOf(session.getHour());
        return hosted ? sessionHours.multiply(new BigDecimal("2.5")) : sessionHours;
    }

    private void addRecognizedHours(Map<String, BigDecimal> recognizedHoursByLoginId, String loginId, BigDecimal recognizedHours) {
        if (recognizedHours.signum() > 0 && recognizedHoursByLoginId.containsKey(loginId)) {
            recognizedHoursByLoginId.merge(loginId, recognizedHours, BigDecimal::add);
        }
    }

    public void authorizeMember(String loginID) {
        Member findMember = getMemberByLoginID(loginID);
        findMember.acceptMember();
        memberRepository.saveAndFlush(findMember);
    }

    public void toggleAdmin(Member admin, String loginID) {
        Member targetMember = getMemberByLoginID(loginID);
        admin.changeRole(targetMember, targetMember.getRoles().hasRole(MemberRole.ADMIN) ?
                MemberRole.MEMBER :
                MemberRole.ADMIN);
        memberRepository.saveAndFlush(targetMember);
    }

    public void selfChangeToRole(Member member, MemberRole role) {
        member.selfChangeRole(role);
        memberRepository.saveAndFlush(member);
    }

    public void changeToRole(Member admin, String targetMemberLoginID, MemberRole role) {
        Member targetMember = getMemberByLoginID(targetMemberLoginID);
        admin.changeRole(targetMember, role);
        memberRepository.saveAndFlush(targetMember);
    }

    public void toggleIsExcepted(Member member, String targetLoginID) {
        Member targetMember = getMemberByLoginID(targetLoginID);
        member.toggleExcept(targetMember);
        memberRepository.saveAndFlush(targetMember);
    }

    public void sendResetPasswordMail(MemberResetRequest request) throws MessagingException {
        String email = request.getEmail();
        String resetPasswordToken = resetMemberPasswordToken(email);

//        mailService.sendEmailPasswordResetToken(email, resetPasswordToken);
    }

    public void resetPassword(MemberResetRequest request) {
        String passwordResetToken = request.getPasswordResetToken();
        String newPassword = request.getNewPassword();

        updateMemberPassword(passwordResetToken, newPassword);
    }

    public void updateMember(Member member, UpdateMemberRequest updateMemberRequest) {
        String encodePassword = passwordHashProvider.encodePassword(updateMemberRequest.getPassword());
        member.updateMemberInfo(updateMemberRequest, encodePassword);
        memberRepository.saveAndFlush(member);
    }

    public void deleteMember(String loginID) {
        memberRepository.delete(getMemberByLoginID(loginID));
    }

    private List<Member> getAllMembers() {
        List<Member> members = memberRepository.findAll();
        members.sort(Comparator.comparing(Member::getName));
        return members;
    }

    @Transactional
    public List<Member> getAllMembersAndUpdateMemberIsExcepted() {
        List<Member> members = getAllMembers();
        members.forEach(member -> {
            member.updateIsExcepted();
            memberRepository.saveAndFlush(member);
        });
        return members;
    }

    public String findNameByLoginID(String loginID) {
        Member member = memberRepository.findByLoginID(loginID)
                .orElseThrow(() -> new NoSuchElementException("No member with given login ID."));
        return member.getName();
    }

    private String resetMemberPasswordToken(String email) {
        Member resetMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("No user found with given email"));
        String resetPasswordToken = RandomString.make(40);
        resetMember.setPasswordResetToken(resetPasswordToken);
        memberRepository.saveAndFlush(resetMember);
        return resetPasswordToken;
    }

    private void updateMemberPassword(String passwordResetToken, String newPassword) {
        Member resetMember = memberRepository.findByPasswordResetToken(passwordResetToken)
                .orElseThrow(() -> new NoSuchElementException("No user found with given passwordResetToken"));
        String newPasswordHash = passwordHashProvider.encodePassword(newPassword);
        resetMember.updatePassword(newPasswordHash);
        memberRepository.saveAndFlush(resetMember);
    }

    public void deleteUnacceptedMembers() {
        getAllMembers().stream().filter(Predicate.not(Member::isEnabled))
                .forEach(memberRepository::delete);
    }
}
