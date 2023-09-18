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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/conversation")
public class MessageController {
    private final MessageService messageService;
    private final MemberService memberService;
    private final NotificationService notificationService;
    private final ConversationService conversationService;

    @PostMapping("/{conversationId}/send")
    public ResponseEntity<?> writeMessage(@AuthenticationPrincipal Member member,
                                          @PathVariable String conversationId,
                                          @RequestBody @Valid MessageCreateRequest request) {
        String senderId = request.getSenderId();
        String receiverId = request.getReceiverId();

        Member receiver = memberService.getMemberByLoginID(receiverId);
        messageService.write(member, receiverId, new MessageCreateValues(conversationId, request));

        if (request.getSenderAnonymous()) senderId = "익명";
        if (request.getReceiverAnonymous()) receiverId = "익명";

        notificationService.createMessageNotification(senderId, receiverId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<MessageResponse> viewMessage(@AuthenticationPrincipal Member member,
                                                       @PathVariable Long messageId) {
        Message message = messageService.findMessageById(member, messageId);
        return ResponseEntity.status(HttpStatus.OK).body(MessageResponse.of(message));
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@AuthenticationPrincipal Member member,
                                              @PathVariable Long messageId) {
        Message message = messageService.findMessageById(member, messageId);
        Conversation conversation = conversationService.findConversationById(message.getConversation().getId(), member.getLoginID());
        if (conversation.getStarterLoginID().equals(member.getLoginID())) {
            messageService.deleteMessageByStarter(member, messageId);
        }
        else messageService.deleteMessageByOther(member, messageId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
