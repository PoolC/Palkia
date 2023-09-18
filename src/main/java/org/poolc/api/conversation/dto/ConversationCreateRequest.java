package org.poolc.api.conversation.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConversationCreateRequest {
    private String starterLoginID;
    private String otherLoginID;
    private boolean starterAnonymous;
    private boolean otherAnonymous;

    protected ConversationCreateRequest() {}
    public ConversationCreateRequest(String starterLoginID, String otherLoginID, boolean starterAnonymous, boolean otherAnonymous) {
        this.starterLoginID = starterLoginID;
        this.otherLoginID = otherLoginID;
        this.starterAnonymous = starterAnonymous;
        this.otherAnonymous = otherAnonymous;
    }

}
