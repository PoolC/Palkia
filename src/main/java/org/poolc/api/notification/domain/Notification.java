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

    @Column(name = "sender_name")
    private String senderName;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "parent_comment_id")
    private Long parentCommentId;

    @Column(name = "notification_type", nullable = false)
    private NotificationType notificationType;

    @Column(name = "read_status", nullable = false, columnDefinition = "boolean default false")
    private Boolean readStatus = false;

    protected Notification() {}

    // 쪽지 알림
    public Notification(String senderId, String receiverId, String senderName, NotificationType notificationType) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.senderName = senderName;
        this.notificationType = notificationType;
    }

    // 뱃지 알림
    public Notification(String receiverId, NotificationType notificationType) {
        this.receiverId = receiverId;
        this.notificationType = notificationType;
    }

    // 댓글 알림
    public Notification(String senderId, String receiverId, String senderName, Long postId, NotificationType notificationType) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.senderName = senderName;
        this.postId = postId;
        this.notificationType = notificationType;
    }

    // 대댓글 알림
    public Notification(String senderId, String receiverId, String senderName, Long postId, Long parentCommentId, NotificationType notificationType) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.senderName = senderName;
        this.postId = postId;
        this.parentCommentId = parentCommentId;
        this.notificationType = notificationType;
    }


    public boolean isRead() { return this.readStatus; }

    public void memberReads() { this.readStatus = true; }
}
