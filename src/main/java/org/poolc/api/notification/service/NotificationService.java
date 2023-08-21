package org.poolc.api.notification.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.member.domain.Member;
import org.poolc.api.notification.domain.Notification;
import org.poolc.api.notification.domain.NotificationType;
import org.poolc.api.notification.dto.NotificationResponse;
import org.poolc.api.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private NotificationRepository notificationRepository;

    public List<Notification> getUnreadNotificationsForMember(Member recipient) {
        return notificationRepository.findByRecipientAndReadStatus(recipient, false)
                .stream()
                .collect(Collectors.toList());
    }

    public List<NotificationResponse> getAllNotificationsForMember(Member recipient) {
        return notificationRepository.findByRecipient(recipient)
                .stream()
                .map(NotificationResponse::of)
                .collect(Collectors.toList());
    }

    public void createNotification(Member sender, Member recipient, NotificationType notificationType) {
        Notification notification = new Notification(sender, recipient, notificationType);
        notificationRepository.save(notification);
    }

    public void readNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NoSuchElementException("No notification with given id."));
        notification.memberReads();
    }
}
