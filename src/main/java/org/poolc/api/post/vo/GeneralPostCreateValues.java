package org.poolc.api.post.vo;

import lombok.Getter;
import org.poolc.api.board.domain.Board;
import org.poolc.api.member.domain.Member;
import org.poolc.api.post.domain.PostType;
import org.poolc.api.post.dto.PostCreateRequest;

import java.util.List;

@Getter
public class GeneralPostCreateValues {
    private final Board board;
    private final Member member;
    private final Boolean anonymous;
    private final String title;
    private final String body;
    private final List<String> fileList;
    private final PostType postType = PostType.GENERAL_POST;

    public GeneralPostCreateValues(Board board, Member member, PostCreateRequest postCreateRequest) {
        this.board = board;
        this.member = member;
        this.anonymous = postCreateRequest.getAnonymous();
        this.title = postCreateRequest.getTitle();
        this.body = postCreateRequest.getBody();
        this.fileList = postCreateRequest.getFileList();
    }

}
