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
    private String starterName;
    private String otherName;
    private MessageResponse lastMessage;

    protected ConversationResponse() {}
    public static ConversationResponse of(Conversation conversation, String starterName, String otherName) {
        ConversationResponse response = new ConversationResponse();
        response.setId(conversation.getId());
        response.setStarterName(starterName);
        response.setOtherName(otherName);
        if (conversation.getLastMessage() != null) response.setLastMessage(MessageResponse.of(conversation.getLastMessage()));
        return response;
    }
}
