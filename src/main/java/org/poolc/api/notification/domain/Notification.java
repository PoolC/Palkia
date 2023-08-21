package org.poolc.api.notification.domain;

import lombok.Getter;
import org.poolc.api.common.domain.TimestampEntity;
import org.poolc.api.member.domain.Member;

import javax.persistence.*;

@Entity
@Getter
@SequenceGenerator(
        name = "NOTIFICATION_SEQ_GENERATOR",
        sequenceName = "NOTIFICATION_SEQ"
)
public class Notification extends TimestampEntity {
    @Id @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTIFICATION_SEQ_GENERATOR")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Member recipient;

    @Column(name = "post_type", nullable = false)
    private NotificationType notificationType;

    @Column(name = "read_status", nullable = false, columnDefinition = "boolean default false")
    private Boolean readStatus;

    protected Notification() {}

    public Notification(Member sender, Member recipient, NotificationType notificationType) {
        this.sender = sender;
        this.recipient = recipient;
        this.notificationType = notificationType;
    }

    public boolean isRead() { return this.readStatus; }

    public void memberReads() { this.readStatus = true; }
}
