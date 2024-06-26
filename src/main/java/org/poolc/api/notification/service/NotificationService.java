package org.poolc.api.notification.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.repository.MemberRepository;
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
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    public List<Notification> getUnreadNotificationsForMember(String receiverId) {
        List<Notification> notifications = notificationRepository.findByReceiverIdAndReadStatus(receiverId, false)
                .stream()
                .sorted(Comparator.comparing(Notification::getCreatedAt).reversed())
                .collect(Collectors.toList());
        notifications.forEach(Notification::memberReads);
        Member recipient = getMemberByLoginID(receiverId);
        recipient.resetNotificationCount();
        return notifications;
    }

    public List<Notification> getAllNotificationsForMember(String receiverId) {
        List<Notification> notifications = notificationRepository.findAllByReceiverId(receiverId)
                .stream()
                .sorted(Comparator.comparing(Notification::getCreatedAt).reversed())
                .collect(Collectors.toList());
        notifications.forEach(Notification::memberReads);
        Member recipient = getMemberByLoginID(receiverId);
        recipient.resetNotificationCount();
        return notifications;
    }

    public void createBadgeNotification(Member receiver) {
        notificationRepository.save(new Notification(receiver.getLoginID(), NotificationType.BADGE));
        receiver.addNotification();
    }

    public void createMessageNotification(String senderId, String receiverId) {
        Member sender = getMemberByLoginID(senderId);
        Member receiver = getMemberByLoginID(receiverId);
        notificationRepository.save(new Notification(senderId, receiverId, sender.getName(), NotificationType.MESSAGE));
        receiver.addNotification();
    }

    public void createCommentNotification(String senderId, String receiverId, Long postId) {
        Member sender = getMemberByLoginID(senderId);
        Member receiver = getMemberByLoginID(receiverId);
        notificationRepository.save(new Notification(senderId, receiverId, sender.getName(), postId, NotificationType.COMMENT));
        receiver.addNotification();
    }

    public void createRecommentNotification(String senderId, String receiverId, Long postId, Long parentCommentId) {
        Member sender = getMemberByLoginID(senderId);
        Member receiver = getMemberByLoginID(receiverId);
        notificationRepository.save(new Notification(senderId, receiverId, sender.getName(), postId, parentCommentId, NotificationType.RECOMMENT));
        receiver.addNotification();
    }

    public void readNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NoSuchElementException("No notification with given id."));
        notification.memberReads();
    }

    private Member getMemberByLoginID(String loginID) {
        return memberRepository.findByLoginID(loginID)
                .orElseThrow(() -> new NoSuchElementException("No user found with given loginID"));
    }
}
