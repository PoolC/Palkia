package org.poolc.api.conversation.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.poolc.api.comment.dto.CommentResponse;
import org.poolc.api.conversation.domain.Conversation;
import org.poolc.api.conversation.dto.ConversationCreateRequest;
import org.poolc.api.conversation.dto.ConversationResponse;
import org.poolc.api.conversation.repository.ConversationRepository;
import org.poolc.api.conversation.vo.ConversationCreateValues;
import org.poolc.api.member.service.MemberService;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConversationService {
    private final ConversationRepository conversationRepository;
    private final MemberService memberService;

    @Transactional
    public ConversationResponse createConversation(String starterLoginID, ConversationCreateRequest request) {
        checkValidParties(starterLoginID, request.getOtherLoginID());
        String conversationId = null;
        if (!request.isStarterAnonymous() && !request.isOtherAnonymous()) {
            conversationId = checkExistingConversation(starterLoginID, request.getOtherLoginID());
        }
        if (conversationId != null) {
            return ConversationResponse.of(conversationRepository.findById(conversationId).get());
        }
        Conversation conversation = new Conversation(new ConversationCreateValues(starterLoginID, request.getOtherLoginID(), request.isStarterAnonymous(), request.isOtherAnonymous()));
        return ConversationResponse.of(conversation);
    }

    @Transactional(readOnly = true)
    public ConversationResponse getConversationResponseById(String conversationId, String loginID) {
        Conversation conversation = findConversationById(conversationId, loginID);
        return ConversationResponse.of(conversation);
    }

    @Transactional(readOnly = true)
    public List<ConversationResponse> findAllConversationsForLoginID(String loginID) {
        return conversationRepository.findAllByStarterLoginIDOrOtherLoginID(loginID, loginID)
                .stream().map(ConversationResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteConversation(String conversationId, String loginID) {
        Conversation conversation = findConversationById(conversationId, loginID);
        checkWhetherInvolved(conversation, loginID);
        boolean sender = findWhetherStarterOrOther(conversation, loginID);
        if (sender) conversation.setStarterDeleted();
        else conversation.setReceiverDeleted();
    }

    public void checkWhetherInvolved(Conversation conversation, String loginID) {
        if (!conversation.getStarterLoginID().equals(loginID) && !conversation.getOtherLoginID().equals(loginID)) {
            throw new IllegalArgumentException("You are not involved in this conversation.");
        }
    }

    public boolean findWhetherStarterOrOther(Conversation conversation, String loginID) {
        return conversation.getStarterLoginID().equals(loginID);
    }

    public Conversation findConversationById(String conversationId, String loginID) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new NoSuchElementException("No conversation found with the given id."));
        checkWhetherInvolved(conversation, loginID);
        return conversation;
    }

    private void checkValidParties(String starterLoginID, String otherLoginID) {
        if (starterLoginID.equals(otherLoginID)) {
            throw new IllegalArgumentException("Sender and receiver cannot be the same person.");
        }
        if (memberService.checkMemberExistsByLoginID(starterLoginID)) {
            throw new NoSuchElementException("Sender is not found.");
        }
        if (memberService.checkMemberExistsByLoginID(otherLoginID)) {
            throw new NoSuchElementException("Receiver is not found.");
        }
     }

    // 두 사용자 모두 실명인 conversation이 존재하는지 확인
    private String checkExistingConversation(String starterLoginID, String otherLoginID) {
        Optional<Conversation> conversationOptional = conversationRepository.findByStarterLoginIDAndOtherLoginIDAndStarterAnonymousFalseAndOtherAnonymousFalse(starterLoginID, otherLoginID);
        if (conversationOptional.isPresent() &&
                !conversationOptional.get().isStarterDeleted() &&
                !conversationOptional.get().isStarterAnonymous() &&
                !conversationOptional.get().isOtherAnonymous()
        ) {
            return conversationOptional.get().getId();
        }
        return null;
    }
}
