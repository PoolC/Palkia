package org.poolc.api.baekjoon;

import lombok.RequiredArgsConstructor;
import org.poolc.api.auth.infra.PasswordHashProvider;
import org.poolc.api.badge.repository.BadgeConditionRepository;
import org.poolc.api.badge.repository.BadgeLogRepository;
import org.poolc.api.baekjoon.domain.Baekjoon;
import org.poolc.api.baekjoon.domain.Problem;
import org.poolc.api.baekjoon.repository.BaekjoonRepository;
import org.poolc.api.baekjoon.repository.ProblemRepository;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.domain.MemberRole;
import org.poolc.api.member.domain.MemberRoles;
import org.poolc.api.member.repository.MemberRepository;
import org.poolc.api.poolc.dto.CreatePoolcRequest;
import org.poolc.api.poolc.service.PoolcService;
import org.poolc.api.poolc.vo.PoolcCreateValues;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BaekjoonDataController {
    private final BaekjoonRepository baekjoonRepository;
    private final ProblemRepository problemRepository;
    private final PasswordHashProvider passwordHashProvider;
    private final PoolcService poolcService;
    private final MemberRepository memberRepository;
    private final BadgeLogRepository badgeLogRepository;
    private final BadgeConditionRepository badgeConditionRepository;

    public void dataInitial(){
        poolcService.createPoolc(new PoolcCreateValues(new CreatePoolcRequest(
                "안유진",
                "01012341234",
                "공학관",
                "",
                "풀씨",
                "",
                false,
                ""
        )));
    }

    public void dataReset(){
        badgeConditionRepository.deleteAll();
        badgeLogRepository.deleteAll();
        baekjoonRepository.deleteAll();
        problemRepository.deleteAll();
        memberRepository.deleteAll();
        Member member = Member.builder()
                .UUID(UUID.randomUUID().toString())
                .loginID("MEMBER_ID")
                .passwordHash(passwordHashProvider.encodePassword("MEMBER_PASSWORD"))
                .email("example@email.com")
                .phoneNumber("010-4444-4444")
                .name("MEMBER_NAME")
                .department("exampleDepartment")
                .studentID("2021147593")
                .passwordResetToken(null)
                .passwordResetTokenValidUntil(null)
                .profileImageURL(null)
                .introduction("")
                .isExcepted(false)
                .roles(MemberRoles.getDefaultFor(MemberRole.MEMBER))
                .build();
        Member member2 = Member.builder()
                .UUID(UUID.randomUUID().toString())
                .loginID("MEMBER_ID2")
                .passwordHash(passwordHashProvider.encodePassword("MEMBER_PASSWORD2"))
                .email("example@email.com2")
                .phoneNumber("010-4444-4442")
                .name("MEMBER_NAME2")
                .department("exampleDepartment2")
                .studentID("2021147521")
                .passwordResetToken(null)
                .passwordResetTokenValidUntil(null)
                .profileImageURL(null)
                .introduction("")
                .isExcepted(false)
                .roles(MemberRoles.getDefaultFor(MemberRole.MEMBER))
                .build();
        Member member3 = Member.builder()
                .UUID(UUID.randomUUID().toString())
                .loginID("MEMBER_ID3")
                .passwordHash(passwordHashProvider.encodePassword("MEMBER_PASSWORD3"))
                .email("example@email.com3")
                .phoneNumber("010-4444-4443")
                .name("MEMBER_NAME3")
                .department("exampleDepartment3")
                .studentID("2021147522")
                .passwordResetToken(null)
                .passwordResetTokenValidUntil(null)
                .profileImageURL(null)
                .introduction("")
                .isExcepted(false)
                .roles(MemberRoles.getDefaultFor(MemberRole.ADMIN))
                .build();
        memberRepository.save(member);
        memberRepository.save(member2);
        memberRepository.save(member3);
        Problem problem = Problem.builder()
                .tags(new ArrayList<>())
                .problemId(1111L)
                .title("토마토")
                .level(6L)
                .build();
        problemRepository.save(problem);
        Baekjoon baekjoon1 = Baekjoon.builder()
                .language("c++")
                .member(member)
                .date(LocalDate.now())
                .problem(problem)
                .submissionId(1234L)
                .build();
        baekjoonRepository.save(baekjoon1);
    }
}
