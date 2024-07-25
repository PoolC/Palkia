package org.poolc.api.notification.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.member.domain.Member;
import org.poolc.api.notification.domain.Notification;
import org.poolc.api.notification.dto.NotificationResponse;
import org.poolc.api.notification.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/{notificationId}")
    public ResponseEntity<NotificationResponse> viewNotification(@AuthenticationPrincipal Member member, @PathVariable("notificationId") Long notificationId) {
        NotificationResponse response = notificationService.readNotification(member, notificationId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications(@AuthenticationPrincipal Member member) {
        List<NotificationResponse> responses = notificationService.getUnreadNotificationsForMember(member.getLoginID());
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/all")
    public ResponseEntity<List<NotificationResponse>> getAllNotifications(@AuthenticationPrincipal Member member) {
        List<Notification> notifications = notificationService.getAllNotificationsForMember(member.getLoginID());
        List<NotificationResponse> responses = notifications.stream()
                .map(NotificationResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }
}
