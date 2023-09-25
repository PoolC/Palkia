package org.poolc.api.comment.vo;

import lombok.Getter;
import org.poolc.api.comment.domain.Comment;
import org.poolc.api.comment.dto.CommentCreateRequest;
import org.poolc.api.member.domain.Member;
import org.poolc.api.post.domain.Post;

@Getter
public class CommentCreateValues {
    private final Post post;
    private final Member member;
    private final Boolean anonymous;
    private final String body;
    private final Long parentId;

    public CommentCreateValues(Post post, Member member, CommentCreateRequest request) {
        this.post = post;
        this.member = member;
        this.anonymous = request.getAnonymous();
        this.body = request.getBody();
        this.parentId = request.getParentId();
    }
}
