package org.poolc.api.notification.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class NotificationSummaryResponse {
    private long unreadCount;
    private List<NotificationResponse> responses;

    public static NotificationSummaryResponse of(long unreadCount, List<NotificationResponse> responses) {
        NotificationSummaryResponse response = new NotificationSummaryResponse();
        response.responses = responses;
        response.unreadCount = unreadCount;
        return response;
    }
}
