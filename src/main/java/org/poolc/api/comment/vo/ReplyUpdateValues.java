package org.poolc.api.comment.vo;

import lombok.Getter;

@Getter
public class ReplyUpdateValues {
    private final Boolean anonymous;
    private final String body;

    public ReplyUpdateValues(Boolean anonymous, String body) {
        this.anonymous = anonymous;
        this.body = body;
    }
}
