package org.poolc.api.message.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.poolc.api.message.domain.Message;

import java.time.LocalDateTime;

@Getter
@Setter(AccessLevel.PRIVATE)
public class MessageResponse {
    private String content;
    private String starterName;
    private String otherName;
    private boolean sentByMe;
    private LocalDateTime sentAt;

    public static MessageResponse of(Message message, boolean sentByMe) {
        MessageResponse response = new MessageResponse();
        response.setContent(message.getContent());
        response.setSentAt(message.getCreatedAt());
        response.setSentByMe(sentByMe);
        response.setStarterName("익명");
        response.setOtherName("익명");
        return response;
    }

    public static MessageResponse of(Message message, boolean sentByMe, String senderName, String receiverName) {
        MessageResponse response = new MessageResponse();
        response.setContent(message.getContent());
        response.setSentAt(message.getCreatedAt());
        response.setSentByMe(sentByMe);
        response.setStarterName(senderName);
        response.setOtherName(receiverName);
        return response;
    }
}
