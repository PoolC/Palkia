package org.poolc.api.post.controller;

import lombok.RequiredArgsConstructor;

import org.poolc.api.like.domain.Subject;
import org.poolc.api.like.service.LikeService;
import org.poolc.api.member.domain.Member;
import org.poolc.api.post.domain.BoardType;
import org.poolc.api.post.domain.Post;
import org.poolc.api.post.dto.GetBoardResponse;
import org.poolc.api.post.dto.PostCreateRequest;
import org.poolc.api.post.dto.PostResponse;
import org.poolc.api.post.dto.PostUpdateRequest;
import org.poolc.api.post.service.PostService;
import org.poolc.api.post.vo.PostCreateValues;
import org.poolc.api.post.vo.PostUpdateValues;
import org.poolc.api.scrap.service.ScrapService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final LikeService likeService;
    private final ScrapService scrapService;

    @PostMapping(value = "/post/new")
    public ResponseEntity<Void> registerPost(@AuthenticationPrincipal Member member,
                                          @RequestBody PostCreateRequest request) {
        postService.createPost(member, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<PostResponse> viewPost(@AuthenticationPrincipal Member member, @PathVariable Long postId) {
        Post post = postService.findById(member, postId);
        PostResponse response = PostResponse.of(post, scrapService.isScrap(member, postId));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/board/{boardTitle}")
    public ResponseEntity<GetBoardResponse> viewPostsByBoard(@AuthenticationPrincipal Member member,
                                                              @PathVariable String boardTitle,
                                                              @RequestParam int page) {
        BoardType boardType = BoardType.getBoardTypeByName(boardTitle);
        GetBoardResponse response = postService.findPostsByBoard(member, boardType, page);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/my_posts")
    public ResponseEntity<GetBoardResponse> viewMyPosts(@AuthenticationPrincipal Member member, @RequestParam int page) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.findPostsByMember(member, page));
    }

    @PutMapping("/post/{postId}")
    public ResponseEntity<Void> updatePost(@AuthenticationPrincipal Member member,
                                           @PathVariable Long postId,
                                           @RequestBody @Valid PostUpdateRequest request) {
        Post post = postService.findById(member, postId);
        postService.updatePost(member, postId, new PostUpdateValues(post.getPostType(), request));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/post/{postId}/like")
    public ResponseEntity<Void> likePost(@AuthenticationPrincipal Member member, @PathVariable Long postId) {
        likeService.like(member, Subject.POST, postId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<Void> deletePost(@AuthenticationPrincipal Member member, @PathVariable Long postId) {
        postService.deletePost(member, postId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/post/search")
    public ResponseEntity<GetBoardResponse> searchPost(@AuthenticationPrincipal Member member,
                                                         @RequestParam String keyword,
                                                         @RequestParam int page) {
        GetBoardResponse postResponses = postService.searchPost(member, keyword, page);
        return ResponseEntity.status(HttpStatus.OK).body(postResponses);
    }
}
