package org.poolc.api.conversation.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.conversation.domain.Conversation;
import org.poolc.api.conversation.dto.ConversationCreateRequest;
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
    public Conversation createConversation(String starterLoginID, ConversationCreateRequest request) {
        checkValidParties(starterLoginID, request.getOtherLoginID());
        String conversationId = null;
        if (!request.isStarterAnonymous() && !request.isOtherAnonymous()) {
            conversationId = checkExistingConversation(starterLoginID, request.getOtherLoginID());
        }
        if (conversationId != null) {
            return conversationRepository.findById(conversationId).get();
        }
        ConversationCreateValues values = convertToConversationCreateValues(starterLoginID, request);
        return conversationRepository.save(new Conversation(values));
    }

    @Transactional(readOnly = true)
    public Conversation findConversationById(String conversationId, String loginID) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new NoSuchElementException("No conversation found with the given id."));
        checkWhetherInvolved(conversation, loginID);
        return conversation;
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

    private ConversationCreateValues convertToConversationCreateValues(String starterID, ConversationCreateRequest request) {
        String receiverName = checkValidParties(starterID, request.getOtherLoginID());
        String senderName = memberService.getMemberByLoginID(request.getOtherLoginID()).getName();
        return new ConversationCreateValues(
                starterID, request.getOtherLoginID(),
                senderName, receiverName,
                request.isStarterAnonymous(), request.isOtherAnonymous()
        );
    }

    private String checkValidParties(String starterLoginID, String otherLoginID) {
        if (starterLoginID.equals(otherLoginID)) {
            throw new IllegalArgumentException("Sender and receiver cannot be the same person.");
        }
        if (memberService.checkMemberExistsByLoginID(starterLoginID)) {
            throw new NoSuchElementException("Sender is not found.");
        }
        if (memberService.checkMemberExistsByLoginID(otherLoginID)) {
            throw new NoSuchElementException("Receiver is not found.");
        }
        return memberService.getMemberByLoginID(otherLoginID).getName();
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
