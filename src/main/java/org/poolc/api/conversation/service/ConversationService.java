package org.poolc.api.conversation.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.conversation.domain.Conversation;
import org.poolc.api.conversation.dto.ConversationCreateRequest;
import org.poolc.api.conversation.repository.ConversationRepository;
import org.poolc.api.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ConversationService {
    private final ConversationRepository conversationRepository;
    private final MemberRepository memberRepository;

    public void createConversation(ConversationCreateRequest request) {
        String receiverName = checkValidParties(request.getSenderLoginID(), request.getReceiverLoginID());
        conversationRepository.save(new Conversation(request.getSenderLoginID(), request.getReceiverLoginID(), receiverName));
    }

    public Conversation findConversationById(String conversationId) {
        return conversationRepository.findById(conversationId)
                .orElseThrow(() -> new NoSuchElementException("No conversation found with the given id."));
    }

    public void deleteConversation(String conversationId, String loginID) {
        Conversation conversation = findConversationById(conversationId);
        checkWhetherInvolved(conversation, loginID);
        boolean sender = findWhetherSenderOrReceiver(conversation, loginID);
        if (sender) conversation.setSenderDeleted();
        else conversation.setReceiverDeleted();
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
}
