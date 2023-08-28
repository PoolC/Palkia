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

    @Column(name = "sender_id", nullable = true)
    private String senderId;

    @Column(name = "receiver_id", nullable = false)
    private String receiverId;

    @Column(name = "notification_type", nullable = false)
    private NotificationType notificationType;

    @Column(name = "read_status", nullable = false, columnDefinition = "boolean default false")
    private Boolean readStatus;

    protected Notification() {}

    public Notification(String senderId, String receiverId, NotificationType notificationType) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.notificationType = notificationType;
    }

    public boolean isRead() { return this.readStatus; }

    public void memberReads() { this.readStatus = true; }
}
