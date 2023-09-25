package org.poolc.api.post.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.poolc.api.comment.domain.Comment;
import org.poolc.api.post.domain.BoardType;
import org.poolc.api.post.domain.JobType;
import org.poolc.api.post.domain.PostType;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostCreateRequest {
    private final BoardType boardType;
    private final Boolean anonymous;
    private final String title;
    private final String body;
    private final List<String> fileList;
    private final PostType postType;
    private Boolean isQuestion;
    private JobType position;
    private String region;
    private String field;
    private LocalDateTime deadline;
    @JsonCreator
    public PostCreateRequest(BoardType boardType, Boolean anonymous, String title, String body, List<String> fileList,
                             PostType postType, Boolean isQuestion,
                             JobType position, String region, String field, LocalDateTime deadline) {
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
