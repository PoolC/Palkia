package org.poolc.api.message.vo;

import lombok.Getter;
import org.poolc.api.message.dto.MessageCreateRequest;

@Getter
public class MessageCreateValues {
    private final String content;
    private final String conversationId;
    private final Boolean senderAnonymous;
    private final Boolean receiverAnonymous;

    public MessageCreateValues(String conversationId, MessageCreateRequest request) {
        this.content = request.getContent();
        this.conversationId = conversationId;
        this.senderAnonymous = request.getSenderAnonymous();
        this.receiverAnonymous = request.getReceiverAnonymous();
    }

}
