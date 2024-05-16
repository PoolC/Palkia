package org.poolc.api.post.dto;

import lombok.AccessLevel;
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
@Setter(AccessLevel.PRIVATE)
public class PostResponse {

    private Long postId;
    private BoardType boardType;
    private String writerLoginId;
    private String writerName;
    private String postProfileImageUrl;
    private Badge badge;
    private String title;
    private String body;
    private LocalDateTime createdAt;
    private List<String> fileList;
    private List<CommentResponse> commentList;
    private PostType postType;
    private Boolean isQuestion;
    private Long boardPostCount;
    private Long boardPageNum;
    private Long likeCount;
    private Long scrapCount;
    private Long commentCount;
    private JobType position;
    private String region;
    private String field;
    private LocalDate deadline;
    private Boolean isScraped;

    public static PostResponse of(Post post, boolean isScraped) {
        PostResponse response = new PostResponse();

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
        response.setFileList(post.getFileList());
        response.setCommentList(
                post.getCommentList().stream()
                        .map(CommentResponse::of)
                        .collect(Collectors.toList())
        );
        response.setCommentCount(post.getCommentCount());
        response.setPostType(post.getPostType());
        response.setPosition(post.getPosition());
        response.setRegion(post.getRegion());
        response.setField(post.getField());
        response.setDeadline(post.getDeadline());
        response.setBoardPostCount(response.getBoardType().getPostCount());
        response.setBoardPageNum(response.getBoardPostCount() / 15 + 1);
        response.setIsScraped(isScraped);
        response.setScrapCount(post.getScrapCount());
        return response;
    }
}
