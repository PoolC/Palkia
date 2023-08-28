package org.poolc.api.post.controller;

import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.poolc.api.auth.exception.UnauthorizedException;
import org.poolc.api.board.domain.Board;
import org.poolc.api.board.service.BoardService;
import org.poolc.api.member.domain.Member;
import org.poolc.api.post.domain.Post;
import org.poolc.api.post.dto.PostCreateRequest;
import org.poolc.api.post.dto.PostResponse;
import org.poolc.api.post.dto.PostUpdateRequest;
import org.poolc.api.post.service.PostService;
import org.poolc.api.post.vo.PostCreateValues;
import org.poolc.api.post.vo.PostUpdateValues;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final BoardService boardService;

    @PostMapping("/post/new")
    public ResponseEntity<?> registerPost(@AuthenticationPrincipal Member member,
                                          @RequestBody @Valid PostCreateRequest request) {
        Board board = boardService.findById(request.getBoardId());
        postService.createPost(new PostCreateValues(board, member, request));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<PostResponse> viewPost(@AuthenticationPrincipal Member member, @PathVariable Long postId) {
        Post post = postService.findPostById(member, postId);
        return ResponseEntity.status(HttpStatus.OK).body(PostResponse.of(post));
    }

    @GetMapping("/board/{boardTitle}")
    public ResponseEntity<List<PostResponse>> viewPostsByBoard(@AuthenticationPrincipal Member member,
                                                              @PathVariable String boardTitle,
                                                              @RequestParam int page) {
        Board board = boardService.findByName(boardTitle);
        List<PostResponse> posts = postService.findPostsByBoard(member, board, page);
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

    @GetMapping("/my_posts")
    public ResponseEntity<List<PostResponse>> viewMyPosts(@AuthenticationPrincipal Member member, @RequestParam int page) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.findPostsByMember(member, page));
    }

    @PutMapping("/post/{postId}")
    public ResponseEntity<Void> updatePost(@AuthenticationPrincipal Member member,
                                           @PathVariable Long postId,
                                           @RequestBody @Valid PostUpdateRequest request) {
        Post post = postService.findPostById(member, postId);
        postService.updatePost(member, postId, new PostUpdateValues(post.getPostType(), request));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<Void> deletePost(@AuthenticationPrincipal Member member, @PathVariable Long postId) {
        postService.deletePost(member, postId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
