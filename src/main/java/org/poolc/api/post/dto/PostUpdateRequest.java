package org.poolc.api.post.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.poolc.api.post.domain.JobType;

import java.time.LocalDateTime;
import java.util.List;

/* */
@Getter
public class PostUpdateRequest {
    private final Boolean anonymous;
    private final String title;
    private final String body;
    private final List<String> fileList;

    private JobType position;
    private String region;
    private String field;
    private LocalDateTime deadline;

    @JsonCreator
    public PostUpdateRequest(Boolean anonymous, String title, String body, List<String> fileList) {
        this.anonymous = anonymous;
        this.title = title;
        this.body = body;
        this.fileList = fileList;
    }

    public PostUpdateRequest(Boolean anonymous, String title, String body, List<String> fileList, JobType position, String region, String field, LocalDateTime deadline) {
        this.anonymous = anonymous;
        this.title = title;
        this.body = body;
        this.fileList = fileList;
        this.position = position;
        this.region = region;
        this.field = field;
        this.deadline = deadline;
    }
}
