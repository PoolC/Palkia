package org.poolc.api.message.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.conversation.domain.Conversation;
import org.poolc.api.conversation.service.ConversationService;
import org.poolc.api.member.domain.Member;
import org.poolc.api.message.domain.Message;
import org.poolc.api.message.dto.MessageResponse;
import org.poolc.api.message.repository.MessageRepository;
import org.poolc.api.message.vo.MessageCreateValues;
import org.poolc.api.notification.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private static final String MESSAGE_NOT_FOUND = "No message found with given id.";
    private static final String ANONYMOUS_NAME = "익명";

    private final MessageRepository messageRepository;
    private final ConversationService conversationService;
    private final NotificationService notificationService;

    @Transactional(readOnly = true)
    public MessageResponse getMessageResponseById(Member member, Long messageId) {
        Message message = findMessageById(member, messageId);
        Conversation conversation = message.getConversation();

        String starterName = conversation.isStarterAnonymous() ? ANONYMOUS_NAME : conversation.getStarterName();
        String otherName = conversation.isOtherAnonymous() ? ANONYMOUS_NAME : conversation.getOtherName();

        // check if member is starter. then check message's sent by starter
        boolean isStarter = conversation.getStarterLoginID().equals(member.getLoginID());
        boolean sentByMe = (isStarter && message.getSentByStarter()) || (!isStarter && !message.getSentByStarter());

        return MessageResponse.of(message, sentByMe, starterName, otherName);
    }

    @Transactional(readOnly = true)
    public List<Message> findMessagesByConversationId(Member member, String conversationId) {
        List<Message> messageList = messageRepository.findAllByConversationId(conversationId);
        Conversation conversation = conversationService.findConversationById(conversationId, member.getLoginID());
        conversationService.checkWhetherInvolved(conversation, member.getLoginID());
        return messageList.stream()
                .filter(message -> !message.isDeleted())
                .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    @Transactional
    public void writeMessage(Member sender, MessageCreateValues values) {
        Conversation conversation = conversationService.findConversationById(values.getConversationId(), sender.getLoginID());
        boolean sentByStarter = conversation.getStarterLoginID().equals(sender.getLoginID());

        Message message = new Message(sender, values.getContent(), conversation, sentByStarter);
        messageRepository.save(message);

        String receiverID = conversation.getStarterLoginID().equals(sender.getLoginID()) ? conversation.getOtherLoginID() : conversation.getStarterLoginID();
        String senderName = ANONYMOUS_NAME;
        String senderID = null;
        if (sentByStarter && !conversation.isStarterAnonymous()) {
            senderName = conversation.getStarterName();
            senderID = conversation.getStarterLoginID();
        }
        if (!sentByStarter && !conversation.isOtherAnonymous()) {
            senderID = conversation.getOtherLoginID();
        }

        notificationService.createMessageNotification(senderID, receiverID, senderName);
    }


    @Transactional
    public void deleteMessage(Member member, Long messageId) {
        Message message = findMessageById(member, messageId);
        conversationService.checkWhetherInvolved(message.getConversation(), member.getLoginID());
        conversationService.findWhetherStarterOrOther(message.getConversation(), member.getLoginID());

        boolean deletedByStarter = message.getConversation().getStarterLoginID().equals(member.getLoginID());
        if (deletedByStarter) {
            message.senderDeletes();
        } else {
            message.otherDeletes();
        }
    }

    private Message findMessageById(Member member, Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException(MESSAGE_NOT_FOUND));
        Conversation conversation = message.getConversation();
        boolean isStarter = conversation.getStarterLoginID().equals(member.getLoginID());
        if (message.isDeleted() || (isStarter && message.getDeletedByStarter()) || !isStarter && message.getDeletedByOther())  {
            throw new NoSuchElementException(MESSAGE_NOT_FOUND);
        }
        conversationService.checkWhetherInvolved(message.getConversation(), member.getLoginID());
        return message;
    }

}
