package org.poolc.api.conversation.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.conversation.dto.ConversationCreateRequest;
import org.poolc.api.conversation.dto.ConversationResponse;
import org.poolc.api.conversation.service.ConversationService;
import org.poolc.api.member.domain.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/conversation")
@Controller
@RequiredArgsConstructor
public class ConversationController {
    private final ConversationService conversationService;

    // create conversation
    @PostMapping("/new")
    public ResponseEntity<String> createConversation(@AuthenticationPrincipal Member member,
                                                     @RequestBody ConversationCreateRequest request) {
        String conversationId = conversationService.createConversation(
                conversationService.convertToConversationCreateValues(request)
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(conversationId);
    }

    // get conversation
    @GetMapping("/{conversationId}")
    public ResponseEntity<ConversationResponse> viewConversation(@AuthenticationPrincipal Member member,
                                                                 @PathVariable String conversationId) {
        ConversationResponse response = ConversationResponse.of(
                conversationService.findConversationById(conversationId, member.getLoginID())
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // delete conversation
    @DeleteMapping("/{conversationId}")
    public ResponseEntity<Void> deleteConversation(@AuthenticationPrincipal Member member,
                                                   @PathVariable String conversationId) {
        conversationService.deleteConversation(conversationId, member.getLoginID());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
