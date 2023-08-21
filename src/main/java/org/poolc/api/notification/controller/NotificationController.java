package org.poolc.api.notification.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.member.domain.Member;
import org.poolc.api.notification.dto.NotificationResponse;
import org.poolc.api.notification.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/{UUID}/unread")
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications(@AuthenticationPrincipal Member member,
                                                                             @PathVariable String UUID) {
        List<NotificationResponse> responses = notificationService.getUnreadNotificationsForMember(member);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/{UUID}")
    public ResponseEntity<List<NotificationResponse>> getAllNotifications(@AuthenticationPrincipal Member member,
                                                                          @PathVariable String UUID) {
        List<NotificationResponse> responses = notificationService.getAllNotificationsForMember(member);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }


}
