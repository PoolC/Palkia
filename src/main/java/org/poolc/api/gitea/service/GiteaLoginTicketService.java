package org.poolc.api.gitea.service;

import org.poolc.api.auth.exception.UnauthenticatedException;
import org.poolc.api.auth.exception.UnauthorizedException;
import org.poolc.api.gitea.dto.GiteaLoginTicketResponse;
import org.poolc.api.gitea.dto.GiteaUserHeaders;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.domain.MemberRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GiteaLoginTicketService {
    private static final int TICKET_BYTES = 32;
    private static final EnumSet<MemberRole> ALLOWED_ROLES = EnumSet.of(
            MemberRole.SUPER_ADMIN,
            MemberRole.ADMIN,
            MemberRole.GRADUATED,
            MemberRole.COMPLETE,
            MemberRole.MEMBER
    );

    private final SecureRandom secureRandom = new SecureRandom();
    private final Map<String, TicketClaims> tickets = new ConcurrentHashMap<>();

    @Value("${gitea.login-ticket.ttl-seconds:60}")
    private long ticketTtlSeconds;

    @Value("${gitea.login-ticket.web-login-url:http://git.dev.poolc.org/_poolc_login}")
    private String webLoginUrl;

    @Value("${gitea.proxy.api-key:${kubernetes.api.key:local-dev}}")
    private String proxyApiKey;

    public GiteaLoginTicketResponse createLoginTicket(Member member) {
        validateAllowedMember(member);
        removeExpiredTickets();

        String ticket = createTicketValue();
        Instant expiresAt = Instant.now().plusSeconds(ticketTtlSeconds);

        tickets.put(ticket, new TicketClaims(GiteaUserHeaders.of(member), expiresAt));

        return new GiteaLoginTicketResponse(ticket, expiresAt, webLoginUrl + "?ticket=" + ticket);
    }

    public GiteaUserHeaders consumeLoginTicket(String apiKey, String ticket) {
        if (!proxyApiKey.equals(apiKey)) {
            throw new UnauthorizedException("Invalid Gitea proxy API key");
        }

        TicketClaims claims = tickets.remove(ticket);
        if (claims == null || claims.isExpired()) {
            throw new UnauthenticatedException("Invalid or expired Gitea login ticket");
        }

        return claims.getHeaders();
    }

    private void validateAllowedMember(Member member) {
        MemberRole role = MemberRole.valueOf(member.getRole());
        if (!ALLOWED_ROLES.contains(role)) {
            throw new UnauthorizedException("This member cannot use Gitea");
        }
    }

    private String createTicketValue() {
        byte[] bytes = new byte[TICKET_BYTES];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private void removeExpiredTickets() {
        tickets.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }

    private static class TicketClaims {
        private final GiteaUserHeaders headers;
        private final Instant expiresAt;

        private TicketClaims(GiteaUserHeaders headers, Instant expiresAt) {
            this.headers = headers;
            this.expiresAt = expiresAt;
        }

        private GiteaUserHeaders getHeaders() {
            return headers;
        }

        private boolean isExpired() {
            return Instant.now().isAfter(expiresAt);
        }
    }
}
