package org.poolc.api.notification.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.service.MemberService;
import org.poolc.api.notification.domain.Notification;
import org.poolc.api.notification.domain.NotificationType;
import org.poolc.api.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private NotificationRepository notificationRepository;
    private MemberService memberService;

    public List<Notification> getUnreadNotificationsForMember(Member recipient) {
        List<Notification> notifications = notificationRepository.findByRecipientAndReadStatus(recipient, false)
                .stream()
                .sorted(Comparator.comparing(Notification::getCreatedAt).reversed())
                .collect(Collectors.toList());
        notifications.forEach(Notification::memberReads);
        recipient.resetNotificationCount();
        return notifications;
    }

    public List<Notification> getAllNotificationsForMember(Member recipient) {
        List<Notification> notifications = notificationRepository.findByRecipient(recipient)
                .stream()
                .sorted(Comparator.comparing(Notification::getCreatedAt).reversed())
                .collect(Collectors.toList());
        notifications.forEach(Notification::memberReads);
        recipient.resetNotificationCount();
        return notifications;
    }

    public void createBadgeNotification(String receiverId) {
        Member receiver = memberService.getMemberByLoginID(receiverId);
        notificationRepository.save(new Notification(receiverId, NotificationType.BADGE));
        receiver.addNotification();
    }

    public void createMessageNotification(String senderId, String receiverId) {
        Member sender = memberService.getMemberByLoginID(senderId);
        Member receiver = memberService.getMemberByLoginID(receiverId);
        notificationRepository.save(new Notification(senderId, receiverId, sender.getName(), NotificationType.MESSAGE));
        receiver.addNotification();
    }

    public void createCommentNotification(String senderId, String receiverId, Long postId) {
        Member sender = memberService.getMemberByLoginID(senderId);
        Member receiver = memberService.getMemberByLoginID(receiverId);
        notificationRepository.save(new Notification(senderId, receiverId, sender.getName(), postId, NotificationType.COMMENT));
        receiver.addNotification();
    }

    public void createRecommentNotification(String senderId, String receiverId, Long postId, Long parentCommentId) {
        Member sender = memberService.getMemberByLoginID(senderId);
        Member receiver = memberService.getMemberByLoginID(receiverId);
        notificationRepository.save(new Notification(senderId, receiverId, sender.getName(), postId, parentCommentId, NotificationType.RECOMMENT));
        receiver.addNotification();
    }

    public void readNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NoSuchElementException("No notification with given id."));
        notification.memberReads();
    }
}
