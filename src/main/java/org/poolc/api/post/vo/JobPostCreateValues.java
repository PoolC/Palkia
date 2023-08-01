package org.poolc.api.post.vo;

import lombok.Getter;
import org.poolc.api.board.domain.Board;
import org.poolc.api.member.domain.Member;
import org.poolc.api.post.domain.JobType;
import org.poolc.api.post.domain.PostType;
import org.poolc.api.post.dto.PostCreateRequest;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class JobPostCreateValues {
    private final Board board;
    private final Member member;
    private final Boolean anonymous;
    private final String title;
    private final String body;
    private final List<String> fileList;
    private final JobType position;
    private final String region;
    private final String field;
    private final LocalDateTime deadline;

    private final PostType postType = PostType.JOB_POST;

    public JobPostCreateValues(Board board, Member member, PostCreateRequest postCreateRequest) {
        this.board = board;
        this.member = member;
        this.anonymous = postCreateRequest.getAnonymous();
        this.title = postCreateRequest.getTitle();
        this.body = postCreateRequest.getBody();
        this.fileList = postCreateRequest.getFileList();
        this.position = postCreateRequest.getPosition();
        this.region = postCreateRequest.getRegion();
        this.field = postCreateRequest.getField();
        this.deadline = postCreateRequest.getDeadline();
    }
}
