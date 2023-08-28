package org.poolc.api.notification.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.poolc.api.notification.domain.Notification;
import org.poolc.api.notification.domain.NotificationType;

import java.time.LocalDateTime;

@Getter
@Setter(AccessLevel.PRIVATE)
public class NotificationResponse {
    private String receiverId;
    private Boolean readStatus;
    private String notificationType;
    private LocalDateTime createdAt;

    private String senderName;
    private String senderId;
    // 댓글 알림이면 포스트로 링크
    private Long postId;

    // 대댓글이면 해당 댓글 주인에게 알림 보내기 위해
    private Long parentCommentId;

    public static NotificationResponse of(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setNotificationType(notification.getNotificationType().getDescription());
        response.setReadStatus(notification.getReadStatus());
        response.setCreatedAt(notification.getCreatedAt());
        response.setReceiverId(notification.getReceiverId());

        if (notification.getNotificationType() != NotificationType.BADGE) {
            response.setSenderId(notification.getSenderId());
            response.setSenderName(notification.getSenderName());
            if (notification.getNotificationType() == NotificationType.COMMENT) {
                response.setPostId(notification.getPostId());
            } else if (notification.getNotificationType() == NotificationType.RECOMMENT) {
                response.setParentCommentId(notification.getParentCommentId());
            }
        }

        return response;
    }
}
