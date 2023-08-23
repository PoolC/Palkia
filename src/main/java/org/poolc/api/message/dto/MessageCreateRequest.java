package org.poolc.api.message.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class MessageCreateRequest {
    private final String content;
    private final String senderUUID;
    private final String receiverUUID;
    private final Boolean senderAnonymous;
    private final Boolean receiverAnonymous;

    @JsonCreator
    public MessageCreateRequest(String content, String senderUUID, String receiverUUID, Boolean senderAnonymous, Boolean receiverAnonymous) {
        this.content = content;
        this.senderUUID = senderUUID;
        this.receiverUUID = receiverUUID;
        this.senderAnonymous = senderAnonymous;
        this.receiverAnonymous = receiverAnonymous;
    }
}
