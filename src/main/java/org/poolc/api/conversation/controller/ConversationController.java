package org.poolc.api.conversation.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.conversation.domain.Conversation;
import org.poolc.api.conversation.dto.ConversationCreateRequest;
import org.poolc.api.conversation.service.ConversationService;
import org.poolc.api.member.domain.Member;
import org.poolc.api.message.dto.MessageResponse;
import org.poolc.api.message.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/conversation")
@Controller
@RequiredArgsConstructor
public class ConversationController {
    private final ConversationService conversationService;
    private final MessageService messageService;

    // create conversation
    @PostMapping("/new")
    public ResponseEntity<String> createConversation(@AuthenticationPrincipal Member member,
                                                     @RequestBody ConversationCreateRequest request) {
        Conversation conversation = conversationService.createConversation(member.getLoginID(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(conversation.getId());
    }

    // get conversation
    @GetMapping("/{conversationId}")
    public ResponseEntity<List<MessageResponse>> viewConversation(@AuthenticationPrincipal Member member,
                                                                 @PathVariable String conversationId) {

        // Conversation conversation = conversationService.findConversationById(conversationId, member.getLoginID());
        List<MessageResponse> responses= messageService.findMessagesByConversationId(member, conversationId)
                .stream()
                .map(message -> messageService.getMessageResponseById(member, message.getId()))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    // delete conversation
    @DeleteMapping("/{conversationId}")
    public ResponseEntity<Void> deleteConversation(@AuthenticationPrincipal Member member,
                                                   @PathVariable String conversationId) {
        conversationService.deleteConversation(conversationId, member.getLoginID());

        messageService.findMessagesByConversationId(member, conversationId)
                .forEach(message -> messageService.deleteMessage(member, message.getId()));
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
