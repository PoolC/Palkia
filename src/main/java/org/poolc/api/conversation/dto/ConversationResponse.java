package org.poolc.api.conversation.dto;

import lombok.Builder;
import lombok.Getter;
import org.poolc.api.conversation.domain.Conversation;

@Getter
public class ConversationResponse {

    private String id;
    private String senderNameOrAnonymous;
    private String receiverNameOrAnonymous;

    protected ConversationResponse() {}
    public ConversationResponse(Conversation conversation) {
        this.id = conversation.getId();
        if (conversation.isSenderAnonymous()) {
            this.senderNameOrAnonymous = "익명";
        } else {
            this.senderNameOrAnonymous = conversation.getSenderName();
        }
        if (conversation.isReceiverAnonymous()) {
            this.receiverNameOrAnonymous = "익명";
        } else {
            this.receiverNameOrAnonymous = conversation.getReceiverName();
        }
    }
}
