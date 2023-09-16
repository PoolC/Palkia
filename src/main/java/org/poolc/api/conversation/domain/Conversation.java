package org.poolc.api.conversation.domain;

import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;
import org.poolc.api.common.domain.TimestampEntity;
import org.poolc.api.conversation.vo.ConversationCreateValues;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "conversations")
public class Conversation extends TimestampEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "conversation_id", columnDefinition = "CHAR(32)")
    private String id;

    @Column(name = "sender_login_id", nullable = false)
    private String senderLoginID;

    @Column(name = "receiver_login_id", nullable = false)
    private String receiverLoginID;

    @Column(name = "sender_name", nullable = false)
    private String senderName;

    @Column(name = "receiver_name", nullable = false)
    private String receiverName;

    @Column(name = "sender_anonymous", nullable = false)
    private boolean senderAnonymous;

    @Column(name = "receiver_anonymous", nullable = false)
    private boolean receiverAnonymous;

    @Column(name = "sender_deleted")
    private boolean senderDeleted = false;

    @Column(name = "receiver_deleted")
    private boolean receiverDeleted = false;

    protected Conversation() {}

    public Conversation(String senderLoginID, String receiverLoginID, String senderName, String receiverName, boolean senderAnonymous, boolean receiverAnonymous) {
        this.senderLoginID = senderLoginID;
        this.receiverLoginID = receiverLoginID;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.senderAnonymous = senderAnonymous;
        this.receiverAnonymous = receiverAnonymous;
    }

    public Conversation(ConversationCreateValues values) {
        this.senderLoginID = values.getSenderLoginID();
        this.receiverLoginID = values.getReceiverLoginID();
        this.senderName = values.getSenderName();
        this.receiverName = values.getReceiverName();
        this.senderAnonymous = values.isSenderAnonymous();
        this.receiverAnonymous = values.isReceiverAnonymous();
    }

    public void setSenderDeleted() {
        this.senderDeleted = true;
    }

    public void setReceiverDeleted() {
        this.receiverDeleted = true;
    }
}
