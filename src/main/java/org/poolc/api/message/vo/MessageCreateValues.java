package org.poolc.api.message.vo;

import lombok.Getter;
import org.poolc.api.message.dto.MessageCreateRequest;

@Getter
public class MessageCreateValues {
    private final String content;
    private final String conversationId;

    public MessageCreateValues(MessageCreateRequest request) {
        this.content = request.getContent();
        this.conversationId = request.getConversationId();
    }

}
