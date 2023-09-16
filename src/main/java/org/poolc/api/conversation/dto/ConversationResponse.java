package org.poolc.api.conversation.dto;

import lombok.AccessLevel;
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
        ConversationResponse response = new ConversationResponse();
        response.setId(conversation.getId());
        if (conversation.isSenderAnonymous()) {
            response.setSenderNameOrAnonymous("익명");
        } else {
            response.setSenderNameOrAnonymous(conversation.getSenderName());
        }
        if (conversation.isReceiverAnonymous()) {
            response.setReceiverNameOrAnonymous("익명");
        } else {
            response.setReceiverNameOrAnonymous(conversation.getReceiverName());
        }
        return response;
    }
}
