package org.poolc.api.comment.vo;

import lombok.Getter;
import org.poolc.api.comment.domain.Comment;
import org.poolc.api.member.domain.Member;
import org.poolc.api.post.domain.GeneralPost;

@Getter
public class CommentCreateValues {
    private final GeneralPost generalPost;
    private final Member member;
    private final Boolean anonymous;
    private final String body;
    private final Comment parent;

    public CommentCreateValues(GeneralPost generalPost, Member member, Boolean anonymous, String body, Comment parent) {
        this.generalPost = generalPost;
        this.member = member;
        this.anonymous = anonymous;
        this.body = body;
        this.parent = parent;
    }
}
