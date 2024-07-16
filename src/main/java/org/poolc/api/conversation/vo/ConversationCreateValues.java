package org.poolc.api.conversation.vo;

import lombok.Getter;

@Getter
public class ConversationCreateValues {
    private String starterLoginID;
    private String otherLoginID;
    private boolean starterAnonymous;
    private boolean otherAnonymous;

    protected ConversationCreateValues() {}

    public ConversationCreateValues(String starterLoginID, String otherLoginID, boolean starterAnonymous, boolean otherAnonymous) {
        this.starterLoginID = starterLoginID;
        this.otherLoginID = otherLoginID;
        this.starterAnonymous = starterAnonymous;
        this.otherAnonymous = otherAnonymous;
    }

}
