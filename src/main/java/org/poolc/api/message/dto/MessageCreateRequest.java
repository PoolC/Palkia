package org.poolc.api.message.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class MessageCreateRequest {
    private final String content;
    private final String senderUUID;
    private final String receiverUUID;
    private final Boolean anonymous;

    @JsonCreator
    public MessageCreateRequest(String content, String senderUUID, String receiverUUID, Boolean anonymous) {
        this.content = content;
        this.senderUUID = senderUUID;
        this.receiverUUID = receiverUUID;
        this.anonymous = anonymous;
    }
}
