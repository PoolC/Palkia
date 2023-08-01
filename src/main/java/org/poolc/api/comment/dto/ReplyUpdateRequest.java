package org.poolc.api.comment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class ReplyUpdateRequest {
    private Boolean anonymous;
    private String body;

    @JsonCreator
    public ReplyUpdateRequest(Boolean anonymous, String body) {
        this.anonymous = anonymous;
        this.body = body;
    }
}
