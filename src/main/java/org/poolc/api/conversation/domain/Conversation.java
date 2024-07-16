package org.poolc.api.conversation.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.poolc.api.common.domain.TimestampEntity;
import org.poolc.api.conversation.vo.ConversationCreateValues;

import javax.persistence.*;
import org.poolc.api.message.domain.Message;

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

    @Column(name = "starter_anonymous", nullable = false)
    private boolean starterAnonymous;

    @Column(name = "other_anonymous", nullable = false)
    private boolean otherAnonymous;

    @Column(name = "starter_deleted")
    private boolean starterDeleted = false;

    @Column(name = "other_deleted")
    private boolean otherDeleted = false;

    @Setter
    @OneToOne
    private Message lastMessage;

    protected Conversation() {}

    public Conversation(String starterLoginID, String otherLoginID, boolean starterAnonymous, boolean otherAnonymous) {
        this.starterLoginID = starterLoginID;
        this.otherLoginID = otherLoginID;
        this.starterAnonymous = starterAnonymous;
        this.otherAnonymous = otherAnonymous;
    }

    public Conversation(ConversationCreateValues values) {
        this.starterLoginID = values.getStarterLoginID();
        this.otherLoginID = values.getOtherLoginID();
        this.starterAnonymous = values.isStarterAnonymous();
        this.otherAnonymous = values.isOtherAnonymous();
    }

    public void setStarterDeleted() {
        this.starterDeleted = true;
    }

    public void setReceiverDeleted() {
        this.otherDeleted = true;
    }

}
