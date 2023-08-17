package org.poolc.api.badge;

import lombok.RequiredArgsConstructor;
import org.poolc.api.auth.infra.PasswordHashProvider;
import org.poolc.api.badge.domain.Badge;
import org.poolc.api.badge.domain.BadgeLog;
import org.poolc.api.badge.repository.BadgeLogRepository;
import org.poolc.api.badge.repository.BadgeRepository;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.domain.MemberRole;
import org.poolc.api.member.domain.MemberRoles;
import org.poolc.api.member.repository.MemberRepository;
import org.poolc.api.poolc.dto.CreatePoolcRequest;
import org.poolc.api.poolc.service.PoolcService;
import org.poolc.api.poolc.vo.PoolcCreateValues;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Profile("badgeTest")
public class BadgeDataLoader implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final BadgeRepository badgeRepository;
    private final PoolcService poolcService;
    private final PasswordHashProvider passwordHashProvider;
    private final BadgeLogRepository badgeLogRepository;

    @Override
    public void run(String... args) throws Exception {
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
        Badge badge1 = Badge.builder()
                .name("test")
                .imageUrl("image.png")
                .description("testBadge")
                .build();
        Badge badge2 = Badge.builder()
                .name("test2")
                .imageUrl("image2.png")
                .description("testBadge2")
                .build();
        badgeRepository.save(badge1);
        badgeRepository.save(badge2);
        BadgeLog badgeLog = BadgeLog.builder()
                .member(member)
                .date(LocalDate.now())
                .badge(badge1)
                .build();
        badgeLogRepository.save(badgeLog);
    }
}
