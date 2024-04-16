package org.poolc.api.scrap.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.member.domain.Member;
import org.poolc.api.post.dto.GetBoardResponse;
import org.poolc.api.post.dto.GetPostsResponse;
import org.poolc.api.post.dto.PostResponse;
import org.poolc.api.post.service.PostService;
import org.poolc.api.scrap.service.ScrapService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/scrap")
@RequiredArgsConstructor
public class ScrapController {
    private final ScrapService scrapService;
    private final PostService postService;

    @GetMapping
    public ResponseEntity<GetBoardResponse> viewMyScraps(@AuthenticationPrincipal Member member,
                                                         @RequestParam int page) {
        GetBoardResponse responses = scrapService.viewMyPosts(member, page);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @PostMapping("/{postId}")
    public ResponseEntity<Void> addScrap(@AuthenticationPrincipal Member member, @PathVariable Long postId) {
        scrapService.scrap(member.getLoginID(), postId);
        postService.scrapPost(member, postId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deleteScrap(@AuthenticationPrincipal Member member, @PathVariable Long postId) {
        scrapService.removeScrap(member.getLoginID(), postId);
        postService.unscrapPost(member, postId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
