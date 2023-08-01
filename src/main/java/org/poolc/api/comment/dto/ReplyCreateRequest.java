package org.poolc.api.comment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class ReplyCreateRequest {
    private final Long questionPostId;
    private final Boolean anonymous;
    private final String body;

    @JsonCreator
    public ReplyCreateRequest(Long questionPostId, Boolean anonymous, String body) {
        this.questionPostId = questionPostId;
        this.anonymous = anonymous;
        this.body = body;
    }
}
