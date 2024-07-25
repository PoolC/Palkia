package org.poolc.api.notification.service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.poolc.api.notification.dto.NotificationResponse;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.repository.MemberRepository;
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

    @Transactional
    public List<NotificationResponse> getUnreadNotificationsForMember(String receiverId) {
        return notificationRepository.findByReceiverIdAndReadStatus(receiverId, false)
                .stream()
                .sorted(Comparator.comparing(Notification::getCreatedAt).reversed())
                .map(NotificationResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Notification> getAllNotificationsForMember(String receiverId) {
        List<Notification> notifications = notificationRepository.findAllByReceiverId(receiverId)
                .stream()
                .peek(Notification::memberReads) // Apply the memberReads method
                .sorted(Comparator.comparing(Notification::getCreatedAt).reversed())
                .collect(Collectors.toList());

        Member recipient = getMemberByLoginID(receiverId);
        if (recipient == null) {
            throw new IllegalArgumentException("Recipient not found for the given receiverId");
        }
        recipient.resetNotificationCount();
        return notifications;
    }
    @Transactional
    public void createBadgeNotification(Member receiver) {
        Notification notification = new Notification(receiver.getLoginID(), NotificationType.BADGE);
        notificationRepository.save(notification);
        receiver.addNotification();
        //sendRealTimeNotification(notification);
    }

    @Transactional
    public void createMessageNotification(String senderId, String receiverId, Long messageId) {
        Member receiver = getMemberByLoginID(receiverId);
        Notification notification = new Notification(senderId, receiverId, messageId, NotificationType.MESSAGE);
        notificationRepository.save(notification);
        receiver.addNotification();
    }

    @Transactional
    public void createCommentNotification(String senderId, String receiverId, Long postId) {
        Member sender = getMemberByLoginID(senderId);
        Member receiver = getMemberByLoginID(receiverId);
        notificationRepository.save(new Notification(senderId, receiverId, postId, NotificationType.POST));
        receiver.addNotification();
    }

    @Transactional
    public void createRecommentNotification(String senderId, String receiverId, Long postId, Long parentCommentId) {
        Member sender = getMemberByLoginID(senderId);
        Member receiver = getMemberByLoginID(receiverId);
        notificationRepository.save(new Notification(senderId, receiverId, parentCommentId, NotificationType.COMMENT));
        receiver.addNotification();
    }

    @Transactional
    public NotificationResponse readNotification(Member member, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NoSuchElementException("No notification found with given id."));
        checkIfSelf(member, notification);
        notification.memberReads();
        return NotificationResponse.of(notification);
    }

    private Member getMemberByLoginID(String loginID) {
        return memberRepository.findByLoginID(loginID)
                .orElseThrow(() -> new NoSuchElementException("No user found with given loginID"));
    }

    private void checkIfSelf(Member member, Notification notification) {
        if (!notification.getReceiverId().equals(member.getLoginID())) throw new NoSuchElementException("No notification found.");
    }
}
