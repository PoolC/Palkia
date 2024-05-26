package org.poolc.api.message.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.poolc.api.common.domain.TimestampEntity;
import org.poolc.api.conversation.domain.Conversation;
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

    @Column(name = "sent_by_starter", nullable = false)
    private Boolean sentByStarter;

    @Column(name = "deleted_by_starter", nullable = false, columnDefinition = "boolean default false")
    private Boolean deletedByStarter;

    @Column(name = "deleted_by_other", nullable = false, columnDefinition = "boolean default false")
    private Boolean deletedByOther;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "conversation", referencedColumnName = "conversation_id", nullable = false)
    private Conversation conversation;

    /*@Column(name = "starter_anonymous", nullable = false)
    private Boolean starterAnonymous;

    @Column(name = "other_anonymous", nullable = false)
    private Boolean otherAnonymous;

    @Column(name = "starter_name")
    private String starterName;

    @Column(name = "other_name")
    private String otherName;
*/
    protected Message() {}

    public Message(Member member, String content, Conversation conversation, Boolean sentByStarter) {
        this.content = content;
        this.conversation = conversation;
        this.sentByStarter = sentByStarter;
        this.deletedByStarter = false;
        this.deletedByOther = false;
    }

    public void senderDeletes() {
        this.deletedByStarter = true;
    }

    public void otherDeletes() {
        this.deletedByOther = true;
    }

    public boolean isDeleted() {
        return this.deletedByStarter && this.deletedByOther;
    }

}
