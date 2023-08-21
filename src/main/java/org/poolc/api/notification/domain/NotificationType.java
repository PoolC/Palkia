package org.poolc.api.notification.domain;

public enum NotificationType {
    MESSAGE("님이 쪽지를 보냈습니다."),
    BADGE("새 뱃지를 받았습니다!"),
    COMMENT("님이 댓글을 달았습니다."),
    RECOMMENT("님이 대댓글을 달았습니다.");

    private final String description;

    private NotificationType(String description) {
        this.description = description;
    }

    public String getDescription() { return this.description; }
}
