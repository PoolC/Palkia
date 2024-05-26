package org.poolc.api.message.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.conversation.domain.Conversation;
import org.poolc.api.conversation.service.ConversationService;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.service.MemberService;
import org.poolc.api.message.domain.Message;
import org.poolc.api.message.dto.MessageCreateRequest;
import org.poolc.api.message.dto.MessageResponse;
import org.poolc.api.message.service.MessageService;
import org.poolc.api.message.vo.MessageCreateValues;
import org.poolc.api.notification.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {
    private final MessageService messageService;
    private final ConversationService conversationService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@AuthenticationPrincipal Member member,
                                          @RequestBody @Valid MessageCreateRequest request) {
        messageService.writeMessage(member, new MessageCreateValues(request));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<MessageResponse> getMessage(@AuthenticationPrincipal Member member,
                                                       @PathVariable Long messageId) {
        MessageResponse response = messageService.getMessageResponseById(member, messageId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@AuthenticationPrincipal Member member,
                                              @PathVariable Long messageId) {
        messageService.deleteMessage(member, messageId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
