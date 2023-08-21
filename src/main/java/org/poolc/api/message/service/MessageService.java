package org.poolc.api.message.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.auth.exception.UnauthorizedException;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.repository.MemberRepository;
import org.poolc.api.message.domain.Message;
import org.poolc.api.message.dto.MessageResponse;
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

    public Message findMessageById(Member member, Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("No message found with the given id."));
        checkSenderOrReceiver(member, message);
        checkIsDeleted(member, message);
        return message;
    }

    public Message findMessageBySenderAndReceiverAndId(Member member, Member other, Long messageId) {
        Message message = messageRepository.findBySenderAndReceiverAndId(member, other, messageId)
                .orElseThrow(() -> new NoSuchElementException("No message found with the given id."));
        checkSenderOrReceiver(member, message);
        checkSenderOrReceiver(other, message);
        checkIsDeleted(member, message);
        return message;
    }

    @Transactional
    public Message write(MessageCreateValues values) {
        Message message = new Message(values.getContent(), values.getSender(), values.getReceiver(), values.getAnonymous());
        messageRepository.save(message);
        return message;
    }

    @Transactional(readOnly = true)
    public List<MessageResponse> viewSentMessages(Member member) {
        List<Message> messages = messageRepository.findAllBySender(member);
        return messages.stream()
                .map(MessageResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MessageResponse> viewReceivedMessages(Member member) {
        List<Message> messages = messageRepository.findAllByReceiver(member);
        return messages.stream()
                .map(MessageResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Message> viewMessagesWith(Member current, Member particular) {
        List<Message> messages = messageRepository.findAllByConversation(current, particular);
        return messages.stream()
                .sorted(Comparator.comparing(Message::getCreatedAt))
                .collect(Collectors.toList());
    }

    public void deleteMessageBySender(Member member, Long messageId) {
        Message message = findMessageById(member, messageId);
        checkSender(member, message);
        message.senderDeletes();
    }

    public void deleteMessageByReceiver(Member member, Long messageId) {
        Message message = findMessageById(member, messageId);
        checkReceiver(member, message);
        message.receiverDeletes();
    }

    private void checkSender(Member member, Message message) {
        if (!member.equals(message.getSender())) throw new UnauthorizedException("접근할 수 없습니다.");
    }
    private void checkReceiver(Member member, Message message) {
        if (!member.equals(message.getReceiver())) throw new UnauthorizedException("접근할 수 없습니다.");
    }
    private void checkSenderOrReceiver(Member member, Message message) {
        checkSender(member, message);
        checkReceiver(member, message);
    }
    private void checkIsDeleted(Member member, Message message) {
        if (message.isDeleted() || (message.getDeletedBySender() && message.getSender().equals(member))
                || message.getDeletedByReceiver() && message.getReceiver().equals(member))
            throw new NoSuchElementException("삭제된 메시지입니다.");
    }
}
