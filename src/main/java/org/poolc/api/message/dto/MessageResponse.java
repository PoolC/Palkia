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
    private boolean sentByStarter;
    private LocalDateTime sentAt;

    public static MessageResponse of(Message message) {
        MessageResponse response = new MessageResponse();
        response.setContent(message.getContent());
        response.setSentAt(message.getCreatedAt());
        response.setSentByStarter(message.getSentByStarter());
        return response;
    }
}
