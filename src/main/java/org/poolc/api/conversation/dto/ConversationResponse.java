package org.poolc.api.conversation.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.poolc.api.conversation.domain.Conversation;
import org.poolc.api.message.dto.MessageResponse;

@Getter
@Setter(AccessLevel.PRIVATE)
public class ConversationResponse {

    private String id;
    private String starterLoginID;
    private String otherLoginID;
    private boolean starterAnonymous;
    private boolean otherAnonymous;
    private MessageResponse lastMessage;

    protected ConversationResponse() {}
    public static ConversationResponse of(Conversation conversation) {
        ConversationResponse response = new ConversationResponse();
        response.setId(conversation.getId());
        response.setStarterLoginID(conversation.getStarterLoginID());
        response.setOtherLoginID(conversation.getOtherLoginID());
        response.setStarterAnonymous(conversation.isStarterAnonymous());
        response.setOtherAnonymous(conversation.isOtherAnonymous());
        if (conversation.getLastMessage() != null) response.setLastMessage(MessageResponse.of(conversation.getLastMessage()));
        return response;
    }
}
