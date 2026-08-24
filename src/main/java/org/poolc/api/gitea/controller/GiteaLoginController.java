package org.poolc.api.gitea.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.gitea.dto.GiteaLoginTicketResponse;
import org.poolc.api.gitea.dto.GiteaUserHeaders;
import org.poolc.api.gitea.service.GiteaLoginTicketService;
import org.poolc.api.member.domain.Member;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gitea")
@RequiredArgsConstructor
public class GiteaLoginController {
    private static final String API_KEY_HEADER = "X-API-KEY";
    private static final String TICKET_HEADER = "X-PoolC-Gitea-Ticket";

    private final GiteaLoginTicketService giteaLoginTicketService;

    @PostMapping("/login-ticket")
    public ResponseEntity<GiteaLoginTicketResponse> createLoginTicket(@AuthenticationPrincipal Member loginMember) {
        if (loginMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(giteaLoginTicketService.createLoginTicket(loginMember));
    }

    @GetMapping("/login-ticket/validate")
    public ResponseEntity<GiteaUserHeaders> validateLoginTicket(
            @RequestHeader(API_KEY_HEADER) String apiKey,
            @RequestHeader(TICKET_HEADER) String ticket
    ) {
        GiteaUserHeaders headers = giteaLoginTicketService.consumeLoginTicket(apiKey, ticket);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("X-WEBAUTH-USER", headers.getLoginID());
        responseHeaders.add("X-WEBAUTH-EMAIL", headers.getEmail());
        responseHeaders.add("X-WEBAUTH-FULLNAME", headers.getName());

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(headers);
    }
}
