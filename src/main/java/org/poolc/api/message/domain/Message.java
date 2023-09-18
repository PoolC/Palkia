package org.poolc.api.message.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.poolc.api.common.domain.TimestampEntity;
import org.poolc.api.member.domain.Member;

import javax.persistence.*;

@Entity
@Getter
@SequenceGenerator(
        name = "MESSAGE_SEQ_GENERATOR",
        sequenceName = "MESSAGE_SEQ"
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message extends TimestampEntity {
    @Id @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MESSAGE_SEQ_GENERATOR")
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "deleted_by_sender", nullable = false, columnDefinition = "boolean default false")
    private Boolean deletedBySender;

    @Column(name = "deleted_by_receiver", nullable = false, columnDefinition = "boolean default false")
    private Boolean deletedByReceiver;

    @Column(name = "conversation_id", nullable = false)
    private String conversationId;

    @Column(name = "sender_anonymous", nullable = false)
    private Boolean senderAnonymous;

    @Column(name = "receiver_anonymous", nullable = false)
    private Boolean receiverAnonymous;

    @Column(name = "sender_name")
    private String senderName;

    @Column(name = "receiver_name")
    private String receiverName;

    protected Message() {}

    public Message(String content, String conversationId, Boolean senderAnonymous, Boolean receiverAnonymous, String senderName, String receiverName) {
        this.content = content;
        this.conversationId = conversationId;
        this.senderAnonymous = senderAnonymous;
        this.receiverAnonymous = receiverAnonymous;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.deletedBySender = false;
        this.deletedByReceiver = false;
    }

    public void senderDeletes() {
        this.deletedBySender = true;
    }

    public void receiverDeletes() {
        this.deletedByReceiver = true;
    }

    public boolean isDeleted() {
        return deletedByReceiver && deletedBySender;
    }


}
