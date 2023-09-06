package org.poolc.api.post.vo;

import lombok.Getter;
import org.poolc.api.comment.domain.Comment;
import org.poolc.api.member.domain.Member;
import org.poolc.api.post.domain.BoardType;
import org.poolc.api.post.domain.JobType;
import org.poolc.api.post.domain.PostType;
import org.poolc.api.post.dto.PostCreateRequest;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostCreateValues {
    private final BoardType boardType;
    private final Member member;
    private final Boolean anonymous;
    private final String title;
    private final String body;
    private final List<String> fileList;
    private final List<Comment> commentList;
    private final PostType postType;
    private Boolean isQuestion;
    private JobType position;
    private String region;
    private String field;
    private LocalDateTime deadline;


    public PostCreateValues(Member member, PostCreateRequest request) {
        this.boardType = request.getBoardType();
        this.member = member;
        this.anonymous = request.getAnonymous();
        this.title = request.getTitle();
        this.body = request.getBody();
        this.fileList = request.getFileList();
        this.commentList = request.getCommentList();
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
