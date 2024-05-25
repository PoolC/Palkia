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
    public Conversation findOrCreateConversation(String starterLoginID, String otherLoginID) {
        checkValidParties(starterLoginID, otherLoginID);
        return conversationRepository.findByStarterLoginIDAndOtherLoginID(starterLoginID, otherLoginID)
                .orElseGet(() -> {
                    ConversationCreateValues values = new ConversationCreateValues(
                            starterLoginID, otherLoginID,
                            memberService.getMemberByLoginID(starterLoginID).getName(),
                            memberService.getMemberByLoginID(otherLoginID).getName(),
                            false, false // defaulting anonymity to false
                    );
                    return conversationRepository.save(new Conversation(values));
                });
    }

    public ConversationCreateValues convertToConversationCreateValues(ConversationCreateRequest request) {
        String receiverName = checkValidParties(request.getStarterLoginID(), request.getOtherLoginID());
        String senderName = memberService.getMemberByLoginID(request.getOtherLoginID()).getName();
        return new ConversationCreateValues(
                request.getStarterLoginID(), request.getOtherLoginID(),
                senderName, receiverName,
                request.isStarterAnonymous(), request.isOtherAnonymous()
        );
    }

    @Transactional(readOnly = true)
    public Conversation findConversationById(String conversationId, String loginID) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new NoSuchElementException("No conversation found with the given id."));
        checkWhetherInvolved(conversation, loginID);
        return conversation;
    }
    public Conversation findConversationByStarterAndOther(String starterLoginID, String otherLoginID) {
        return conversationRepository.findByStarterLoginIDAndOtherLoginID(starterLoginID, otherLoginID)
                .orElseThrow(() -> new NoSuchElementException("No conversation found with the given parties."));
    }

    @Transactional
    public void deleteConversation(String conversationId, String loginID) {
        Conversation conversation = findConversationById(conversationId, loginID);
        checkWhetherInvolved(conversation, loginID);
        boolean sender = findWhetherSenderOrReceiver(conversation, loginID);
        if (sender) conversation.setSenderDeleted();
        else conversation.setReceiverDeleted();
    }

    public void checkWhetherInvolved(Conversation conversation, String loginID) {
        if (!conversation.getStarterLoginID().equals(loginID) && !conversation.getOtherLoginID().equals(loginID)) {
            throw new IllegalArgumentException("You are not involved in this conversation.");
        }
    }
    public boolean findWhetherSenderOrReceiver(Conversation conversation, String loginID) {
        return conversation.getStarterLoginID().equals(loginID);
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

    private String checkExistingConversation(String starterLoginID, String otherLoginID) {
        Optional<Conversation> conversationOptional = conversationRepository.findByStarterLoginIDAndOtherLoginID(starterLoginID, otherLoginID);
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
