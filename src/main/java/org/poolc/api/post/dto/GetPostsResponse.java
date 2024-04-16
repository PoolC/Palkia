package org.poolc.api.post.dto;

import lombok.Getter;
import lombok.Setter;
import org.poolc.api.badge.domain.Badge;
import org.poolc.api.comment.dto.CommentResponse;
import org.poolc.api.post.domain.BoardType;
import org.poolc.api.post.domain.JobType;
import org.poolc.api.post.domain.Post;
import org.poolc.api.post.domain.PostType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class GetPostsResponse {
    private Long postId;
    private BoardType boardType;
    private String writerLoginId;
    private String writerName;
    private String postProfileImageUrl;
    private Badge badge;
    private String title;
    private String body;
    private LocalDateTime createdAt;
    private PostType postType;
    private Boolean isQuestion;
    private Long likeCount;
    private Long scrapCount;
    private Long commentCount;
    private JobType position;
    private String region;
    private String field;
    private LocalDate deadline;

    public static GetPostsResponse of(Post post) {
        GetPostsResponse response = new GetPostsResponse();

        if (post.getIsDeleted()) return null;
        if (!post.getAnonymous()) {
            response.setWriterName(post.getMember().getName());
            response.setWriterLoginId(post.getMember().getLoginID());
            response.setPostProfileImageUrl(post.getMember().getProfileImageURL());
            response.setBadge(post.getMember().getBadge());
        }
        if (post.getPostType() == PostType.GENERAL_POST) response.setIsQuestion(post.getIsQuestion());

        response.setPostId(post.getId());
        response.setBoardType(post.getBoardType());
        response.setTitle(post.getTitle());
        response.setBody(post.getBody());
        response.setCreatedAt(post.getCreatedAt());
        response.setCommentCount(post.getCommentCount());
        response.setPostType(post.getPostType());
        response.setPosition(post.getPosition());
        response.setRegion(post.getRegion());
        response.setField(post.getField());
        response.setDeadline(post.getDeadline());
        response.setScrapCount(post.getScrapCount());
        return response;
    }
}
