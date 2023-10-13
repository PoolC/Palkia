package org.poolc.api.post.vo;

import lombok.Getter;
import org.poolc.api.comment.domain.Comment;
import org.poolc.api.member.domain.Member;
import org.poolc.api.post.domain.BoardType;
import org.poolc.api.post.domain.JobType;
import org.poolc.api.post.domain.PostType;
import org.poolc.api.post.dto.PostCreateRequest;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PostCreateValues {

    @NotBlank
    private final BoardType boardType;

    @NotBlank
    private final Member member;

    @NotBlank
    private final Boolean anonymous;

    @NotBlank
    private final String title;

    @NotBlank
    private final String body;

    private final List<String> fileList;
    private final List<Comment> commentList;

    @NotBlank
    private final PostType postType;
    private Boolean isQuestion;
    private JobType position;
    private String region;
    private String field;
    private LocalDate deadline;


    public PostCreateValues(Member member, PostCreateRequest request) {
        this.boardType = request.getBoardType();
        this.member = member;
        this.anonymous = request.getAnonymous();
        this.title = request.getTitle();
        this.body = request.getBody();
        this.fileList = request.getFileList();
        this.commentList = new ArrayList<>();
        this.postType = request.getPostType();
        if (request.getPostType() == PostType.JOB_POST) {
            this.position = request.getPosition();
            this.region = request.getRegion();
            this.field = request.getField();
            this.deadline = request.getDeadline();
        } else {
            this.isQuestion = request.getIsQuestion();
        }
    }
}
