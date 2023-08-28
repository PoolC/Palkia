package org.poolc.api.post.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.board.domain.Board;
import org.poolc.api.board.domain.BoardName;
import org.poolc.api.board.service.BoardService;
import org.poolc.api.like.domain.Subject;
import org.poolc.api.like.service.LikeService;
import org.poolc.api.member.domain.Member;
import org.poolc.api.post.domain.Post;
import org.poolc.api.post.dto.PostCreateRequest;
import org.poolc.api.post.dto.PostResponse;
import org.poolc.api.post.dto.PostUpdateRequest;
import org.poolc.api.post.service.PostService;
import org.poolc.api.post.vo.PostCreateValues;
import org.poolc.api.post.vo.PostUpdateValues;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final BoardService boardService;
    private final LikeService likeService;

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
        BoardName boardName = BoardName.getByDescription(boardTitle);
        Board board = boardService.findByBoardName(boardName);
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

    @PostMapping("/post/{postId}/like")
    public ResponseEntity<Void> likePost(@AuthenticationPrincipal Member member, @PathVariable Long postId) {
        likeService.like(member.getLoginID(), Subject.POST, postId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<Void> deletePost(@AuthenticationPrincipal Member member, @PathVariable Long postId) {
        postService.deletePost(member, postId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
