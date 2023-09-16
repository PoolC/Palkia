package org.poolc.api.conversation.vo;

import lombok.Getter;

@Getter
public class ConversationCreateValues {
    private String senderLoginID;
    private String receiverLoginID;
    private String senderName;
    private String receiverName;
    private boolean senderAnonymous;
    private boolean receiverAnonymous;

    protected ConversationCreateValues() {}

    public ConversationCreateValues(String senderLoginID, String receiverLoginID, String senderName, String receiverName, boolean senderAnonymous, boolean receiverAnonymous) {
        this.senderLoginID = senderLoginID;
        this.receiverLoginID = receiverLoginID;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.senderAnonymous = senderAnonymous;
        this.receiverAnonymous = receiverAnonymous;
    }

}
