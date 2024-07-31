package org.poolc.api.post.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.poolc.api.comment.domain.Comment;
import org.poolc.api.post.domain.JobType;

import java.time.LocalDate;
import java.util.List;

/* */
@Getter
public class PostUpdateRequest {
    private final Boolean anonymous;
    private final String title;
    private final String body;
    private final List<String> fileList;
    private final List<Comment> commentList;
    private JobType position;
    private String region;
    private String field;
    private LocalDate deadline;

    @JsonCreator
    public PostUpdateRequest(Boolean anonymous, String title, String body, List<String> fileList, List<Comment> commentList, JobType position,
                             String region, String field, LocalDate deadline) {
        this.anonymous = anonymous;
        this.title = title;
        this.body = body;
        this.fileList = fileList;
        this.commentList = commentList;
        this.position = position;
        this.region = region;
        this.field = field;
        this.deadline = deadline;
    }
}
