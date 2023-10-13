package org.poolc.api.comment.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.poolc.api.badge.domain.Badge;
import org.poolc.api.comment.domain.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter(AccessLevel.PRIVATE)
public class CommentResponse {
    private Long commentId;
    private Long postId;
    private String writerLoginId;
    private String writerName;
    private String profileImageUrl;
    private Badge badge;
    private Boolean anonymous;
    private String body;
    private Long parentCommentId;
    private List<CommentResponse> children;
    private Long likeCount;
    private LocalDateTime createdAt;

    public CommentResponse() {}

    public static CommentResponse of(Comment comment) {
        CommentResponse response = new CommentResponse();

        if (comment.getIsDeleted()) {
            // 삭제된 자식 없는 댓글
            if (!comment.hasChildren()) return null;
            // 삭제된 자식 있는 댓글
            else response.setBody("삭제된 댓글입니다.");
        } else {
            response.setCommentId(comment.getId());
            response.setPostId(comment.getPost().getId());
            response.setCreatedAt(comment.getCreatedAt());
            response.setBody(comment.getBody());
            // 익명 아닌 댓글
            if (!comment.getAnonymous()) {
                response.setWriterLoginId(comment.getMember().getLoginID());
                response.setWriterName(comment.getMember().getName());
                response.setProfileImageUrl(comment.getMember().getProfileImageURL());
                response.setBadge(comment.getMember().getBadge());
            }
            // 답변은 좋아요 개수 포함
            if (comment.getPost().getIsQuestion()) response.setLikeCount(comment.getLikeCount());
            if (comment.hasChildren()) {
                response.setChildren(comment.getChildren()
                        .stream().map(CommentResponse::of)
                        .collect(Collectors.toList()));
            }
            if (comment.getIsChild()) response.setParentCommentId(comment.getParent().getId());
        }
        return response;
    }
}
