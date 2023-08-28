package org.poolc.api.message.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class MessageCreateRequest {
    private final String content;
    private final String senderId;
    private final String receiverId;
    private final Boolean senderAnonymous;
    private final Boolean receiverAnonymous;

    @JsonCreator
    public MessageCreateRequest(String content, String senderId, String receiverId, Boolean senderAnonymous, Boolean receiverAnonymous) {
        this.content = content;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.senderAnonymous = senderAnonymous;
        this.receiverAnonymous = receiverAnonymous;
    }
}
