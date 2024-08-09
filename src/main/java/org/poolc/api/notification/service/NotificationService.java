package org.poolc.api.notification.service;

import org.poolc.api.notification.dto.NotificationResponse;
import org.poolc.api.notification.dto.NotificationSummaryResponse;
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
    public NotificationSummaryResponse getUnreadNotificationsForMember(Member member) {
        List<NotificationResponse> responses = notificationRepository.findByReceiverIdAndReadStatus(member.getLoginID(), false)
                .stream()
                .sorted(Comparator.comparing(Notification::getCreatedAt).reversed())
                .map(NotificationResponse::of)
                .collect(Collectors.toList());
        long unreadCount = notificationRepository.countByReceiverIdAndReadIsFalse(member.getLoginID());
        return NotificationSummaryResponse.of(unreadCount, responses);
    }

    @Transactional
    public NotificationSummaryResponse getAllNotificationsForMember(Member member) {
        List<NotificationResponse> responses = notificationRepository.findAllByReceiverId(member.getLoginID())
                .stream()
                //.peek(Notification::memberReads)
                .sorted(Comparator.comparing(Notification::getCreatedAt).reversed())
                .map(NotificationResponse::of)
                .collect(Collectors.toList());
        long unreadCount = notificationRepository.countByReceiverIdAndReadIsFalse(member.getLoginID());
        return NotificationSummaryResponse.of(unreadCount, responses);
    }

    @Transactional
    public void readAllNotifications(Member member) {
        List<Notification> notifications = notificationRepository.findAllByReceiverId(member.getLoginID());
        notifications.forEach(Notification::memberReads);
    }

    @Transactional
    public void createBadgeNotification(Member receiver) {
        Notification notification = new Notification(receiver.getLoginID(), NotificationType.BADGE);
        notificationRepository.save(notification);
        //sendRealTimeNotification(notification);
    }

    @Transactional
    public void createMessageNotification(String senderId, String receiverId, Long messageId) {
        Member receiver = getMemberByLoginID(receiverId);
        Notification notification = new Notification(senderId, receiverId, messageId, NotificationType.MESSAGE);
        notificationRepository.save(notification);
    }

    @Transactional
    public void createCommentNotification(String senderId, String receiverId, Long postId) {
        Member sender = getMemberByLoginID(senderId);
        Member receiver = getMemberByLoginID(receiverId);
        notificationRepository.save(new Notification(senderId, receiverId, postId, NotificationType.POST));
    }

    @Transactional
    public void createRecommentNotification(String senderId, String receiverId, Long postId, Long parentCommentId) {
        Member sender = getMemberByLoginID(senderId);
        Member receiver = getMemberByLoginID(receiverId);
        notificationRepository.save(new Notification(senderId, receiverId, parentCommentId, NotificationType.COMMENT));
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
