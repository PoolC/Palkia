package org.poolc.api.comment.vo;

import lombok.Getter;
import org.poolc.api.member.domain.Member;
import org.poolc.api.post.domain.QuestionPost;

@Getter
public class ReplyCreateValues {
    private final QuestionPost questionPost;
    private final Member member;
    private final Boolean anonymous;
    private final String body;
    private final Long likeCount;

    public ReplyCreateValues(QuestionPost questionPost, Member member, Boolean anonymous, String body, Long likeCount) {
        this.questionPost = questionPost;
        this.member = member;
        this.anonymous = anonymous;
        this.body = body;
        this.likeCount = likeCount;
    }
}
