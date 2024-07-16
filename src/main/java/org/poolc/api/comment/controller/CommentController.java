package org.poolc.api.comment.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.comment.domain.Comment;
import org.poolc.api.comment.dto.CommentCreateRequest;
import org.poolc.api.comment.dto.CommentResponse;
import org.poolc.api.comment.dto.CommentUpdateRequest;
import org.poolc.api.comment.service.CommentService;
import org.poolc.api.comment.vo.CommentCreateValues;
import org.poolc.api.comment.vo.CommentUpdateValues;
import org.poolc.api.like.domain.Subject;
import org.poolc.api.like.service.LikeService;
import org.poolc.api.member.domain.Member;
import org.poolc.api.notification.service.NotificationService;
import org.poolc.api.post.domain.Post;
import org.poolc.api.post.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@AuthenticationPrincipal Member member,
                                                         @RequestBody @Valid CommentCreateRequest request) {
        CommentResponse response = commentService.createComment(member, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(@AuthenticationPrincipal Member member,
                                              @PathVariable Long commentId,
                                              @RequestBody CommentUpdateRequest request) {
        commentService.updateComment(member, commentId, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@AuthenticationPrincipal Member member, @PathVariable Long commentId) {
        commentService.deleteComment(member, commentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<Void> likeComment(@AuthenticationPrincipal Member member, @PathVariable Long commentId) {
        Comment comment = commentService.findById(commentId);
        if (!member.equals(comment.getMember())) likeService.like(member.getLoginID(), Subject.COMMENT, commentId);
        else throw new IllegalArgumentException("본인의 댓글은 좋아할 수 없습니다.");
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
