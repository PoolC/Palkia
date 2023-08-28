package org.poolc.api.message.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.service.MemberService;
import org.poolc.api.message.domain.Message;
import org.poolc.api.message.dto.MessageCreateRequest;
import org.poolc.api.message.dto.MessageResponse;
import org.poolc.api.message.service.MessageService;
import org.poolc.api.message.vo.MessageCreateValues;
import org.poolc.api.notification.domain.NotificationType;
import org.poolc.api.notification.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;
    private final MemberService memberService;
    private final NotificationService notificationService;

    @PostMapping("/new")
    public ResponseEntity<?> writeMessage(@AuthenticationPrincipal Member member,
                                          @RequestBody @Valid MessageCreateRequest request) {
        String senderId = request.getSenderId();
        String receiverId = request.getReceiverId();

        Member sender = memberService.getMemberByLoginID(senderId);
        Member receiver = memberService.getMemberByLoginID(receiverId);
        messageService.write(new MessageCreateValues(sender, receiver, request));

        if (request.getSenderAnonymous()) senderId = "익명";
        if (request.getReceiverAnonymous()) receiverId = "익명";

        notificationService.createMessageNotification(senderId, receiverId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{memberUUID}/{messageId}")
    public ResponseEntity<MessageResponse> viewMessage(@AuthenticationPrincipal Member member,
                                                       @PathVariable String memberUUID,
                                                       @PathVariable Long messageId) {
        Member other = memberService.getMemberByLoginID(memberUUID);
        Message message = messageService.findMessageBySenderAndReceiverAndId(member, other, messageId);
        return ResponseEntity.status(HttpStatus.OK).body(MessageResponse.of(message));
    }

    @DeleteMapping("/{memberUUID}/{messageId}")
    public ResponseEntity<Void> deleteMessage(@AuthenticationPrincipal Member member,
                                              @PathVariable String memberUUID,
                                              @PathVariable Long messageId) {
        Member other = memberService.getMemberByLoginID(memberUUID);
        Message message = messageService.findMessageById(member, messageId);
        if (message.getSender().equals(member)) messageService.deleteMessageBySender(member, messageId);
        else messageService.deleteMessageByReceiver(member, messageId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{memberUUID}")
    public ResponseEntity<List<MessageResponse>> viewConversation(@AuthenticationPrincipal Member member,
                                                                  @PathVariable String UUID) {
        Member other = memberService.getMemberByLoginID(UUID);
        List<Message> conversation = messageService.viewMessagesWith(member, other);
        List<MessageResponse> conversationResponse = conversation.stream()
                .map(MessageResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(conversationResponse);
    }
    @DeleteMapping("/{memberUUID}")
    public ResponseEntity<Void> deleteConversation(@AuthenticationPrincipal Member member,
                                              @PathVariable String memberUUID) {
        Member other = memberService.getMemberByLoginID(memberUUID);
        List<Message> conversation = messageService.viewMessagesWith(member, other);
        conversation.stream()
                .map(message ->
                {
                    if (message.getSender().equals(member)) {
                        messageService.deleteMessageBySender(member, message.getId());
                    } else {
                        messageService.deleteMessageByReceiver(member, message.getId());
                    }
                    return null;
                });
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
