package org.poolc.api.notification.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.poolc.api.notification.domain.Notification;

@Getter
@Setter(AccessLevel.PRIVATE)
public class NotificationResponse {
    public String notificationType;
    public String senderName;
    public Boolean readStatus;

    public static NotificationResponse of(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setNotificationType(notification.getNotificationType().getDescription());
        response.setReadStatus(notification.getReadStatus());

        if (notification.getSender() != null) response.setSenderName(notification.getSender().getName());
        else response.setSenderName("익명");

        return response;
    }
}
