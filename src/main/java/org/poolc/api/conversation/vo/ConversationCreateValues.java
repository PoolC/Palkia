package org.poolc.api.conversation.vo;

import lombok.Getter;

@Getter
public class ConversationCreateValues {
    private String starterLoginID;
    private String otherLoginID;
    private String starterName;
    private String otherName;
    private boolean starterAnonymous;
    private boolean otherAnonymous;

    protected ConversationCreateValues() {}

    public ConversationCreateValues(String starterLoginID, String otherLoginID, String starterName, String otherName, boolean starterAnonymous, boolean otherAnonymous) {
        this.starterLoginID = starterLoginID;
        this.otherLoginID = otherLoginID;
        this.starterName = starterName;
        this.otherName = otherName;
        this.starterAnonymous = starterAnonymous;
        this.otherAnonymous = otherAnonymous;
    }

}
