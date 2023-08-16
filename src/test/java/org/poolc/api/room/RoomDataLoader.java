package org.poolc.api.room;

import lombok.RequiredArgsConstructor;
import org.poolc.api.auth.infra.PasswordHashProvider;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.domain.MemberRole;
import org.poolc.api.member.domain.MemberRoles;
import org.poolc.api.member.repository.MemberRepository;
import org.poolc.api.poolc.dto.CreatePoolcRequest;
import org.poolc.api.poolc.service.PoolcService;
import org.poolc.api.poolc.vo.PoolcCreateValues;
import org.poolc.api.room.domain.Room;
import org.poolc.api.room.repository.RoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Profile("roomTest")
public class RoomDataLoader implements CommandLineRunner {

    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final PoolcService poolcService;
    private final PasswordHashProvider passwordHashProvider;

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
        roomRepository.save(new Room(LocalDate.of(2023,1,1),
                LocalTime.of(10,0),
                LocalTime.of(12,0),
                "테스트",member
                ));
        roomRepository.save(new Room(LocalDate.of(2023,1,1),
                LocalTime.of(14,0),
                LocalTime.of(16,0),
                "테스트",member
        ));
        roomRepository.save(new Room(LocalDate.of(2023,1,1),
                LocalTime.of(12,0),
                LocalTime.of(14,0),
                "테스트",member
        ));
        roomRepository.save(new Room(LocalDate.of(2023,1,1),
                LocalTime.of(16,0),
                LocalTime.of(18,0),
                "테스트",member
        ));
    }
}
