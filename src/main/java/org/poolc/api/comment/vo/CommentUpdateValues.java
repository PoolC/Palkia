package org.poolc.api.comment.vo;

import lombok.Getter;
import org.poolc.api.comment.dto.CommentUpdateRequest;

@Getter
public class CommentUpdateValues {
    private final Boolean anonymous;
    private final String body;

    public CommentUpdateValues(CommentUpdateRequest commentUpdateRequest) {
        this.anonymous = commentUpdateRequest.getAnonymous();
        this.body = commentUpdateRequest.getBody();
    }
}