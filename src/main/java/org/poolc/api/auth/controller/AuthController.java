package org.poolc.api.auth.controller;

import org.poolc.api.auth.dto.AuthRequest;
import org.poolc.api.auth.dto.AuthResponse;
import org.poolc.api.auth.service.AuthService;
import org.poolc.api.member.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponse> createAccessToken(@RequestBody AuthRequest request) {
        String token = authService.createAccessToken(request.getLoginID(), request.getPassword());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping(path = "/token",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponse> reissueToken(@AuthenticationPrincipal Member member){
        String token = authService.reIssueAccessToken(member);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
