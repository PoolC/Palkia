package org.poolc.api.message.vo;

import lombok.Getter;
import org.poolc.api.member.domain.Member;
import org.poolc.api.message.dto.MessageCreateRequest;

@Getter
public class MessageCreateValues {
    private final String content;
    private final Member sender;
    private final Member receiver;

    public MessageCreateValues(Member sender, Member receiver, MessageCreateRequest request) {
        this.content = request.getContent();
        this.sender = sender;
        this.receiver = receiver;
    }
}
