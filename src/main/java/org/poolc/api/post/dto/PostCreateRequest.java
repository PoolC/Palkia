package org.poolc.api.post.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.poolc.api.post.domain.BoardType;
import org.poolc.api.post.domain.JobType;
import org.poolc.api.post.domain.PostType;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;

@Getter
public class PostCreateRequest {
    @NotEmpty
    private final BoardType boardType;

    @NotEmpty
    private final Boolean anonymous;

    @NotEmpty
    private final String title;

    @NotEmpty
    private final String body;

    private final List<String> fileList;

    @NotEmpty
    private final PostType postType;

    private Boolean isQuestion;
    private JobType position;
    private String region;
    private String field;
    private LocalDate deadline;
    @JsonCreator
    public PostCreateRequest(BoardType boardType, Boolean anonymous, String title, String body, List<String> fileList,
                             PostType postType, Boolean isQuestion, JobType position, String region, String field, LocalDate deadline) {
        this.boardType = boardType;
        this.anonymous = anonymous;
        this.title = title;
        this.body = body;
        this.fileList = fileList;
        this.postType = postType;
        if (postType == PostType.JOB_POST) {
            this.position = position;
            this.region = region;
            this.field = field;
            this.deadline = deadline;
        } else {
            this.isQuestion = isQuestion;
        }
    }
}
