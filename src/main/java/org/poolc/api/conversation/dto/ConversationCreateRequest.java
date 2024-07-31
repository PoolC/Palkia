package org.poolc.api.conversation.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConversationCreateRequest {
    private String otherLoginID;
    private boolean starterAnonymous;
    private boolean otherAnonymous;

    protected ConversationCreateRequest() {}
    public ConversationCreateRequest(String otherLoginID, boolean starterAnonymous, boolean otherAnonymous) {
        this.otherLoginID = otherLoginID;
        this.starterAnonymous = starterAnonymous;
        this.otherAnonymous = otherAnonymous;
    }

}
