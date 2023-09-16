package org.poolc.api.conversation.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.conversation.domain.Conversation;
import org.poolc.api.conversation.dto.ConversationCreateRequest;
import org.poolc.api.conversation.repository.ConversationRepository;
import org.poolc.api.conversation.vo.ConversationCreateValues;
import org.poolc.api.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConversationService {
    private final ConversationRepository conversationRepository;
    private final MemberRepository memberRepository;

    public String createConversation(ConversationCreateValues values) {
        checkValidParties(values.getSenderLoginID(), values.getReceiverLoginID());
        String conversationId = checkExistingConversation(values.getSenderLoginID(), values.getReceiverLoginID());
        if (conversationId != null) return conversationId;
        else conversationRepository.save(new Conversation(values));
        Conversation conversation = findConversationByReceiverAndSender(values.getSenderLoginID(), values.getReceiverLoginID());
        return conversation.getId();
    }

    public ConversationCreateValues convertToConversationCreateValues(ConversationCreateRequest request) {
        String receiverName = checkValidParties(request.getSenderLoginID(), request.getReceiverLoginID());
        String senderName = memberRepository.findByLoginID(request.getSenderLoginID()).get().getName();
        return new ConversationCreateValues(
                request.getSenderLoginID(), request.getReceiverLoginID(),
                senderName, receiverName,
                request.isSenderAnonymous(), request.isReceiverAnonymous()
        );
    }

    public Conversation findConversationById(String conversationId, String loginID) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new NoSuchElementException("No conversation found with the given id."));
        checkWhetherInvolved(conversation, loginID);
        return conversation;
    }

    public Conversation findConversationByReceiverAndSender(String senderLoginID, String receiverLoginID) {
        return conversationRepository.findBySenderLoginIDAndReceiverLoginID(senderLoginID, receiverLoginID)
                .orElseThrow(() -> new NoSuchElementException("No conversation found with the given parties."));
    }

    public void deleteConversation(String conversationId, String loginID) {
        Conversation conversation = findConversationById(conversationId, loginID);
        checkWhetherInvolved(conversation, loginID);
        boolean sender = findWhetherSenderOrReceiver(conversation, loginID);
        if (sender) conversation.setSenderDeleted();
        else conversation.setReceiverDeleted();
        // message 완성한 다음에 stream해서 삭제된 것들은 싹 삭제해야 함
    }

    private boolean findWhetherSenderOrReceiver(Conversation conversation, String loginID) {
        return conversation.getSenderLoginID().equals(loginID);
    }

    private String checkValidParties(String senderLoginId, String receiverLoginId) {
        if (senderLoginId.equals(receiverLoginId)) {
            throw new IllegalArgumentException("Sender and receiver cannot be the same person.");
        }
        if (!memberRepository.existsByLoginId(senderLoginId)) {
            throw new NoSuchElementException("Sender is not found.");
        }
        if (!memberRepository.existsByLoginId(receiverLoginId)) {
            throw new NoSuchElementException("Receiver is not found.");
        }
        return memberRepository.findByLoginID(receiverLoginId).get().getName();
    }

    private void checkWhetherInvolved(Conversation conversation, String loginID) {
        if (!conversation.getSenderLoginID().equals(loginID) && !conversation.getReceiverLoginID().equals(loginID)) {
            throw new IllegalArgumentException("You are not involved in this conversation.");
        }
    }

    private String checkExistingConversation(String senderLoginID, String receiverLoginID) {
        Optional<Conversation> conversationOptional = conversationRepository.findBySenderLoginIDAndReceiverLoginID(senderLoginID, receiverLoginID);
        if (conversationOptional.isPresent() &&
                !conversationOptional.get().isSenderDeleted() &&
                !conversationOptional.get().isSenderAnonymous() &&
                !conversationOptional.get().isReceiverAnonymous()
        ) {
            return conversationOptional.get().getId();
        }
        return null;
    }
}
