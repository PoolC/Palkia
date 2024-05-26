package org.poolc.api.message.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class MessageCreateRequest {
    private final String content;
    private final String conversationId;

    @JsonCreator
    public MessageCreateRequest(@JsonProperty("content") String content, @JsonProperty("conversationId") String conversationId) {
        this.content = content;
        this.conversationId = conversationId;
    }
}
