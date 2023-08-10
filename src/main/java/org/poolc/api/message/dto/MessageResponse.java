package org.poolc.api.message.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.poolc.api.message.domain.Message;

import java.time.LocalDateTime;

@Getter
@Setter(AccessLevel.PRIVATE)
public class MessageResponse {
    private String content;
    private String senderName;
    private String receiverName;
    private LocalDateTime sentAt;

    public static MessageResponse of(Message message) {
        MessageResponse response = new MessageResponse();
        response.setContent(message.getContent());
        response.setSenderName(message.getSender().getName());
        response.setReceiverName(message.getReceiver().getName());
        response.setSentAt(message.getCreatedAt());
        return response;
    }
}
