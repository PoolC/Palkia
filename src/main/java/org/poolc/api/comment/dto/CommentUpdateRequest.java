package org.poolc.api.comment.dto;

import lombok.Getter;

@Getter
public class CommentUpdateRequest {
    private final Boolean anonymous;
    private final String body;

    public CommentUpdateRequest(Boolean anonymous, String body) {
        this.anonymous = anonymous;
        this.body = body;
    }
}
