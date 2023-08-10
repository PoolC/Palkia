package org.poolc.api.message.repository;

import org.poolc.api.member.domain.Member;
import org.poolc.api.message.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query(value = "SELECT m from Message m WHERE m.receiver = :receiver")
    List<Message> findAllByReceiver(Member receiver);
    @Query(value = "SELECT m from Message m WHERE m.sender = :sender")
    List<Message> findAllBySender(Member sender);
    @Query(value = "SELECT m from Message m WHERE (m.sender = :one AND m.receiver = :two) OR (m.sender = :two AND m.receiver = :one)")
    List<Message> findAllByConversation(Member one, Member two);
    @Query(value = "SELECT m from Message m WHERE m.sender = :sender AND m.receiver = :receiver AND m.id = :id")
    Optional<Message> findBySenderAndReceiverAndId(Member sender, Member receiver, Long id);
}
