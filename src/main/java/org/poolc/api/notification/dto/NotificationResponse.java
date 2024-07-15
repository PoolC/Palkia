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
    private Boolean readStatus;
    private NotificationType notificationType;
    private LocalDateTime createdAt;
    private String senderName;
    // 댓글 알림이면 포스트로 링크
    private Long causedById;

    public static NotificationResponse of(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setNotificationType(notification.getNotificationType());
        response.setReadStatus(notification.getReadStatus());
        response.setCreatedAt(notification.getCreatedAt());

        if (notification.getNotificationType() != NotificationType.BADGE) {
            response.setSenderName(notification.getSenderName());
            response.setCausedById(notification.getCausedById());
        }

        return response;
    }
}
