package org.poolc.api.gitea.service;

import org.junit.jupiter.api.Test;
import org.poolc.api.auth.exception.UnauthorizedException;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.domain.MemberRole;
import org.poolc.api.member.domain.MemberRoles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GiteaLoginTicketServiceTest {
    private final GiteaLoginTicketService service = createService();

    @Test
    void allowedPoolcRolesCanCreateGiteaLoginTicket() {
        Set<MemberRole> allowedRoles = Set.of(
                MemberRole.SUPER_ADMIN,
                MemberRole.ADMIN,
                MemberRole.TECHNICIAN,
                MemberRole.MEMBER,
                MemberRole.GRADUATED,
                MemberRole.COMPLETE
        );

        allowedRoles.forEach(role ->
                assertThat(service.createLoginTicket(member(role)).getTicket()).isNotBlank()
        );
    }

    @Test
    void blockedPoolcRolesCannotCreateGiteaLoginTicket() {
        Set<MemberRole> blockedRoles = Set.of(
                MemberRole.UNACCEPTED,
                MemberRole.EXPELLED,
                MemberRole.QUIT,
                MemberRole.PUBLIC,
                MemberRole.INACTIVE
        );

        blockedRoles.forEach(role ->
                assertThatThrownBy(() -> service.createLoginTicket(member(role)))
                        .isInstanceOf(UnauthorizedException.class)
        );
    }

    private static GiteaLoginTicketService createService() {
        GiteaLoginTicketService service = new GiteaLoginTicketService();
        ReflectionTestUtils.setField(service, "ticketTtlSeconds", 60L);
        ReflectionTestUtils.setField(service, "webLoginUrl", "http://git.dev.poolc.org/_poolc_login");
        ReflectionTestUtils.setField(service, "proxyApiKey", "test-gitea-proxy-key");
        return service;
    }

    private static Member member(MemberRole role) {
        return Member.builder()
                .UUID(UUID.randomUUID().toString())
                .loginID(role.name() + "_ID")
                .passwordHash("PASSWORD_HASH")
                .email(role.name().toLowerCase() + "@email.com")
                .phoneNumber("010-0000-0000")
                .name(role.name() + "_NAME")
                .department("exampleDepartment")
                .studentID(UUID.randomUUID().toString())
                .roles(MemberRoles.getDefaultFor(role))
                .build();
    }
}
