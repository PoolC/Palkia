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
    private String receiverName;
    private LocalDateTime sentAt;

    public static MessageResponse of(Message message) {
        MessageResponse response = new MessageResponse();
        response.setContent(message.getContent());
        response.setSentAt(message.getCreatedAt());

        if (message.getReceiverAnonymous()) response.setReceiverName("익명");
        else response.setReceiverName(message.getReceiverName());

        return response;
    }
}
