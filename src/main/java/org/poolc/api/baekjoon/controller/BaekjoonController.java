package org.poolc.api.baekjoon.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.baekjoon.dto.GetMyBaekjoonResponse;
import org.poolc.api.baekjoon.dto.PostBaekjoonRequest;
import org.poolc.api.baekjoon.service.BaekjoonService;
import org.poolc.api.member.domain.Member;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/baekjoon", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class BaekjoonController {
    private final BaekjoonService baekjoonService;

    @GetMapping
    public ResponseEntity<GetMyBaekjoonResponse> getMyBaekjoon(@AuthenticationPrincipal Member member){
        GetMyBaekjoonResponse response = GetMyBaekjoonResponse.builder()
                .data(baekjoonService.getMyBaekjoon(member))
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<Void> solve(@AuthenticationPrincipal Member member, @RequestBody PostBaekjoonRequest postBaekjoonRequest){
        baekjoonService.solveProblem(member, postBaekjoonRequest.getProblemId(), postBaekjoonRequest.getSubmissionId(), postBaekjoonRequest.getTitle(), postBaekjoonRequest.getLevel(),postBaekjoonRequest.getProblemTags(), postBaekjoonRequest.getLanguage());
        return ResponseEntity.ok().build();
    }

}
