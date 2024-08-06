package org.poolc.api.conversation.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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
    private static final String ANONYMOUS_NAME = "익명";
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
            return getExistingConversationResponse(conversationId);
        }
        Conversation conversation = createNewConversation(starterLoginID, request);
        return buildConversationResponse(conversation);
    }


    @Transactional(readOnly = true)
    public ConversationResponse getConversationResponseById(String conversationId, String loginID) {
        Conversation conversation = findConversationById(conversationId, loginID);
        String[] names = findNamesForConversation(conversation);
        return ConversationResponse.of(conversation, names[0], names[1]);
    }

    @Transactional(readOnly = true)
    public List<ConversationResponse> findAllConversationsForLoginID(String loginID) {
        return conversationRepository.findAllByStarterLoginIDOrOtherLoginID(loginID, loginID)
                .stream()
                .sorted(Comparator.comparing(Conversation::getCreatedAt).reversed())
                .map(conversation -> {
                    String[] names = findNamesForConversation(conversation);
                    return ConversationResponse.of(conversation, names[0], names[1]);
                })
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

    private Conversation createNewConversation(String starterLoginID, ConversationCreateRequest request) {
        Conversation conversation = new Conversation(
                new ConversationCreateValues(starterLoginID, request.getOtherLoginID(), request.isStarterAnonymous(), request.isOtherAnonymous())
        );
        conversationRepository.save(conversation);
        return conversation;
    }
    private ConversationResponse buildConversationResponse(Conversation conversation) {
        String[] names = findNamesForConversation(conversation);
        return ConversationResponse.of(conversation, names[0], names[1]);
    }
    private ConversationResponse getExistingConversationResponse(String conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId).orElseThrow(() -> new NoSuchElementException("Conversation not found"));
        String[] names = findNamesForConversation(conversation);
        return ConversationResponse.of(conversation, names[0], names[1]);
    }
    private String[] findNamesForConversation(Conversation conversation) {
        String starterName = ANONYMOUS_NAME, otherName = ANONYMOUS_NAME;
        if (!conversation.isStarterAnonymous()) starterName = memberService.findNameByLoginID(conversation.getStarterLoginID());
        if (!conversation.isOtherAnonymous()) otherName = memberService.findNameByLoginID(conversation.getOtherLoginID());
        return new String[] {starterName, otherName};
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
