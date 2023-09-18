package org.poolc.api.message.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.conversation.domain.Conversation;
import org.poolc.api.conversation.service.ConversationService;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.service.MemberService;
import org.poolc.api.message.domain.Message;
import org.poolc.api.message.repository.MessageRepository;
import org.poolc.api.message.vo.MessageCreateValues;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final ConversationService conversationService;
    private final MemberService memberService;

    public Message findMessageById(Member member, Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("No message found with given id."));
        if (message.isDeleted()) throw new NoSuchElementException("No message found with given id.");
        conversationService.checkWhetherInvolved(message.getConversation(), member.getLoginID());
        return message;
    }

    public List<Message> findMessagesByConversationId(Member member, String conversationId) {
        List<Message> messageList = messageRepository.findAllByConversationID(conversationId);
        Conversation conversation = conversationService.findConversationById(conversationId, member.getLoginID());
        conversationService.checkWhetherInvolved(conversation, member.getLoginID());
        return messageList.stream()
                .filter(message -> !message.isDeleted())
                .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    @Transactional
    public Message write(Member member, String receiverLoginID, MessageCreateValues values) {
        String senderName = member.getName();
        String receiverName = memberService.getMemberByLoginID(receiverLoginID).getName();
        String starterId = conversationService.findConversationById(values.getConversationId(), member.getLoginID())
                .getStarterLoginID();
        Conversation conversation = conversationService.findConversationById(values.getConversationId(), member.getLoginID());
        boolean starterIsSender = member.getLoginID().equals(starterId);
        Message message = new Message(
                values.getContent(),
                starterIsSender,
                conversation,
                values.getSenderAnonymous(),
                values.getReceiverAnonymous(),
                senderName,
                receiverName
        );
        messageRepository.save(message);
        return message;
    }

    public void deleteMessageByStarter(Member member, Long messageId) {
        Message message = findMessageById(member, messageId);
        conversationService.checkWhetherInvolved(message.getConversation(), member.getLoginID());
        conversationService.findWhetherSenderOrReceiver(message.getConversation(), member.getLoginID());
        message.starterDeletes();
    }

    public void deleteMessageByOther(Member member, Long messageId) {
        Message message = findMessageById(member, messageId);
        conversationService.checkWhetherInvolved(message.getConversation(), member.getLoginID());
        conversationService.findWhetherSenderOrReceiver(message.getConversation(), member.getLoginID());
        message.otherDeletes();
    }

}
