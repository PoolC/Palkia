package org.poolc.api.conversation.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.conversation.domain.Conversation;
import org.poolc.api.conversation.dto.ConversationCreateRequest;
import org.poolc.api.conversation.dto.ConversationResponse;
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

    // get all conversations
    @GetMapping("/all")
    public ResponseEntity<List<ConversationResponse>> getAllConversations(@AuthenticationPrincipal Member member) {
        return ResponseEntity.status(HttpStatus.OK).body(conversationService.findAllConversationsForLoginID(member.getLoginID()));
    }

    // create conversation
    @PostMapping("/new")
    public ResponseEntity<ConversationResponse> createConversation(@AuthenticationPrincipal Member member,
                                                     @RequestBody ConversationCreateRequest request) {
        ConversationResponse response = conversationService.createConversation(member.getLoginID(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // get conversation
    @GetMapping("/{conversationId}")
    public ResponseEntity<List<MessageResponse>> viewConversation(@AuthenticationPrincipal Member member,
                                                                 @PathVariable String conversationId) {

        return ResponseEntity.status(HttpStatus.OK).body(messageService.findMessageResponsesByConversationId(member, conversationId));
    }

    // delete conversation
    @DeleteMapping("/{conversationId}")
    public ResponseEntity<Void> deleteConversation(@AuthenticationPrincipal Member member,
                                                   @PathVariable String conversationId) {
        conversationService.deleteConversation(member.getLoginID(), conversationId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
