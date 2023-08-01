package org.poolc.api.comment.dto;

import lombok.Builder;
import lombok.Getter;
import org.poolc.api.comment.domain.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class CommentResponse {
    private final Long commentId;
    private final Long generalPostId;
    private final String writerLoginId;
    private final String writerName;
    private final Boolean anonymous;
    private final String body;
    private final Boolean isDeleted;
    private final Boolean isChild;
    private final Long parentCommentId;
    private final List<CommentResponse> children;
    private final LocalDateTime createdAt;

    public static CommentResponse of(Comment comment) {
        List<CommentResponse> children = comment.getChildren()
                .stream().map(CommentResponse::of)
                .collect(Collectors.toList());
        if (comment.getIsDeleted() && !comment.hasChildren()) {
            return null;
        } else if (comment.getIsDeleted() && comment.hasChildren()) {
            return CommentResponse.builder()
                    .commentId(comment.getId())
                    .generalPostId(comment.getGeneralPost().getId())
                    .writerLoginId(null)
                    .writerName(null)
                    .body("삭제된 댓글입니다.")
                    .isDeleted(true)
                    .isChild(false)
                    .parentCommentId(null)
                    .children(children)
                    .createdAt(comment.getCreatedAt())
                    .build();
        } else if (comment.getAnonymous()) {
            return CommentResponse.builder()
                    .commentId(comment.getId())
                    .generalPostId(comment.getGeneralPost().getId())
                    .writerLoginId(null)
                    .writerName(null)
                    .body(comment.getBody())
                    .isDeleted(false)
                    .isChild(comment.getIsChild())
                    .parentCommentId(comment.getParent().getId())
                    .children(children)
                    .createdAt(comment.getCreatedAt())
                    .build();
        } else {
            return CommentResponse.builder()
                    .commentId(comment.getId())
                    .generalPostId(comment.getGeneralPost().getId())
                    .writerLoginId(comment.getMember().getLoginID())
                    .writerName(comment.getMember().getName())
                    .body(comment.getBody())
                    .isDeleted(false)
                    .isChild(comment.getIsChild())
                    .parentCommentId(comment.getParent().getId())
                    .children(children)
                    .createdAt(comment.getCreatedAt())
                    .build();
        }
    }
}
