package org.poolc.api.comment.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.comment.domain.Comment;
import org.poolc.api.comment.dto.CommentCreateRequest;
import org.poolc.api.comment.dto.CommentResponse;
import org.poolc.api.comment.dto.CommentUpdateRequest;
import org.poolc.api.comment.service.CommentService;
import org.poolc.api.comment.vo.CommentCreateValues;
import org.poolc.api.comment.vo.CommentUpdateValues;
import org.poolc.api.member.domain.Member;
import org.poolc.api.notification.domain.NotificationType;
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
    private final PostService postService;
    private final CommentService commentService;
    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@AuthenticationPrincipal Member member,
                                                         @RequestBody @Valid CommentCreateRequest request) {
        Post post = postService.findPostById(member, request.getPostId());

        Comment parent;
        if (request.getIsChild()) parent = commentService.findById(request.getParentId());
        else parent = null;

        CommentCreateValues values = new CommentCreateValues(post, member, request.getAnonymous(), request.getBody(), parent);
        Comment newComment = commentService.createComment(values);
        // 대댓글을 달면 댓글쓴 사람한테 알림
        // 댓글 달면 포스트 쓴 사람한테 알림

        if (parent == null) notificationService.createNotification(member.getUUID(), post.getMember().getUUID(), NotificationType.COMMENT);
        else notificationService.createNotification(member.getUUID(), parent.getMember().getUUID(), NotificationType.RECOMMENT);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommentResponse.of(newComment));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(@AuthenticationPrincipal Member member,
                                              @PathVariable Long commentId,
                                              @RequestBody CommentUpdateRequest request) {
        commentService.updateComment(member, commentId, new CommentUpdateValues(request));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@AuthenticationPrincipal Member member, @PathVariable Long commentId) {
        commentService.deleteComment(member, commentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
