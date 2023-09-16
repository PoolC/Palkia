package org.poolc.api.conversation.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConversationCreateRequest {
    private String senderLoginID;
    private String receiverLoginID;
    private String receiverName;

    protected ConversationCreateRequest() {}
    public ConversationCreateRequest(String senderLoginID, String receiverLoginID, String receiverName) {
        this.senderLoginID = senderLoginID;
        this.receiverLoginID = receiverLoginID;
        this.receiverName = receiverName;
    }

}
