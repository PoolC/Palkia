package org.poolc.api.conversation.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConversationCreateRequest {
    private String senderLoginID;
    private String receiverLoginID;
    private boolean senderAnonymous;
    private boolean receiverAnonymous;

    protected ConversationCreateRequest() {}
    public ConversationCreateRequest(String senderLoginID, String receiverLoginID, boolean senderAnonymous, boolean receiverAnonymous) {
        this.senderLoginID = senderLoginID;
        this.receiverLoginID = receiverLoginID;
        this.senderAnonymous = senderAnonymous;
        this.receiverAnonymous = receiverAnonymous;
    }

}
