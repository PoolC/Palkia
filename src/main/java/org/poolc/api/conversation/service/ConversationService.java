package org.poolc.api.conversation.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.conversation.domain.Conversation;
import org.poolc.api.conversation.dto.ConversationCreateRequest;
import org.poolc.api.conversation.repository.ConversationRepository;
import org.poolc.api.conversation.vo.ConversationCreateValues;
import org.poolc.api.member.service.MemberService;
import org.poolc.api.message.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConversationService {
    private final ConversationRepository conversationRepository;
    private final MemberService memberService;

    public String createConversation(ConversationCreateValues values) {
        checkValidParties(values.getStarterLoginID(), values.getOtherLoginID());
        String conversationId = checkExistingConversation(values.getStarterLoginID(), values.getOtherLoginID());
        if (conversationId != null) return conversationId;
        else conversationRepository.save(new Conversation(values));
        Conversation conversation = findConversationByReceiverAndSender(values.getStarterLoginID(), values.getOtherLoginID());
        return conversation.getId();
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

    public Conversation findConversationById(String conversationId, String loginID) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new NoSuchElementException("No conversation found with the given id."));
        checkWhetherInvolved(conversation, loginID);
        return conversation;
    }

    public Conversation findConversationByReceiverAndSender(String senderLoginID, String receiverLoginID) {
        return conversationRepository.findByStarterLoginIDAndOtherLoginID(senderLoginID, receiverLoginID)
                .orElseThrow(() -> new NoSuchElementException("No conversation found with the given parties."));
    }

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

    private String checkValidParties(String senderLoginId, String receiverLoginId) {
        if (senderLoginId.equals(receiverLoginId)) {
            throw new IllegalArgumentException("Sender and receiver cannot be the same person.");
        }
        if (memberService.checkMemberExistsByLoginID(senderLoginId)) {
            throw new NoSuchElementException("Sender is not found.");
        }
        if (memberService.checkMemberExistsByLoginID(receiverLoginId)) {
            throw new NoSuchElementException("Receiver is not found.");
        }
        return memberService.getMemberByLoginID(receiverLoginId).getName();
    }

    private String checkExistingConversation(String senderLoginID, String receiverLoginID) {
        Optional<Conversation> conversationOptional = conversationRepository.findByStarterLoginIDAndOtherLoginID(senderLoginID, receiverLoginID);
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
