package org.poolc.api.message.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class MessageCreateRequest {
    private final String content;
    private final String conversationId;

    @JsonCreator
    public MessageCreateRequest(String content, String conversationId) {
        this.content = content;
        this.conversationId = conversationId;
    }
}
