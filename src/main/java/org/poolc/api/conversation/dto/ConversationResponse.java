package org.poolc.api.conversation.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.poolc.api.conversation.domain.Conversation;

@Getter
@Setter(AccessLevel.PRIVATE)
public class ConversationResponse {

    private String id;
    private String senderNameOrAnonymous;
    private String receiverNameOrAnonymous;

    protected ConversationResponse() {}
    public static ConversationResponse of(Conversation conversation) {
        ConversationResponse conversationResponse = new ConversationResponse();
        conversationResponse.setId(conversation.getId());
        if (conversation.isSenderAnonymous()) {
            conversationResponse.setSenderNameOrAnonymous("익명");
        } else {
            conversationResponse.setSenderNameOrAnonymous(conversation.getSenderName());
        }
        if (conversation.isReceiverAnonymous()) {
            conversationResponse.setReceiverNameOrAnonymous("익명");
        } else {
            conversationResponse.setReceiverNameOrAnonymous(conversation.getReceiverName());
        }
    }
}
