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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Member receiver;

    @Column(name = "anonymous", nullable = false, columnDefinition = "boolean default false")
    private Boolean anonymous;

    public Message(String content, Member sender, Member receiver, Boolean anonymous) {
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
        this.anonymous = anonymous;
    }

    public Message() {}

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
