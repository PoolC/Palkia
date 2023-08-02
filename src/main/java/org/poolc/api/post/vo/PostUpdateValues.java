package org.poolc.api.post.vo;

import lombok.Getter;
import org.poolc.api.comment.domain.Comment;
import org.poolc.api.post.domain.JobType;
import org.poolc.api.post.domain.PostType;
import org.poolc.api.post.dto.PostUpdateRequest;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostUpdateValues {
    private final Boolean anonymous;
    private final String title;
    private final String body;
    private final List<String> fileList;
    private final List<Comment> commentList;
    private Boolean isQuestion;
    private JobType position;
    private String region;
    private String field;
    private LocalDateTime deadline;

    public PostUpdateValues(PostType postType, PostUpdateRequest request) {
        this.anonymous = request.getAnonymous();
        this.title = request.getTitle();
        this.body = request.getBody();
        this.fileList = request.getFileList();
        this.commentList = request.getCommentList();
        if (postType == PostType.GENERAL_POST) {
            this.isQuestion = request.getIsQuestion();
        } else {
            this.position = request.getPosition();
            this.region = request.getRegion();
            this.field = request.getField();
            this.deadline = request.getDeadline();
        }
    }

}
