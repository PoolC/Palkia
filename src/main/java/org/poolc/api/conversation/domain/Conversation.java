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

    @Column(name = "starter_login_id", nullable = false)
    private String starterLoginID;

    @Column(name = "other_login_id", nullable = false)
    private String otherLoginID;

    @Column(name = "starter_name", nullable = false)
    private String starterName;

    @Column(name = "other_name", nullable = false)
    private String otherName;

    @Column(name = "starter_anonymous", nullable = false)
    private boolean starterAnonymous;

    @Column(name = "other_anonymous", nullable = false)
    private boolean otherAnonymous;

    @Column(name = "starter_deleted")
    private boolean starterDeleted = false;

    @Column(name = "other_deleted")
    private boolean otherDeleted = false;

    protected Conversation() {}

    public Conversation(String starterLoginID, String otherLoginID, String starterName, String otherName, boolean starterAnonymous, boolean otherAnonymous) {
        this.starterLoginID = starterLoginID;
        this.otherLoginID = otherLoginID;
        this.starterName = starterName;
        this.otherName = otherName;
        this.starterAnonymous = starterAnonymous;
        this.otherAnonymous = otherAnonymous;
    }

    public Conversation(ConversationCreateValues values) {
        this.starterLoginID = values.getSenderLoginID();
        this.otherLoginID = values.getReceiverLoginID();
        this.starterName = values.getSenderName();
        this.otherName = values.getReceiverName();
        this.starterAnonymous = values.isSenderAnonymous();
        this.otherAnonymous = values.isReceiverAnonymous();
    }

    public void setSenderDeleted() {
        this.starterDeleted = true;
    }

    public void setReceiverDeleted() {
        this.otherDeleted = true;
    }
}
