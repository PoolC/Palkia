package org.poolc.api.message.repository;

import java.util.Optional;

import org.poolc.api.conversation.domain.Conversation;
import org.poolc.api.message.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByConversationId(String conversationID);

    @Query("SELECT m FROM Message m WHERE m.conversation.id = :conversationId ORDER BY m.createdAt DESC")
    Optional<Message> findTopByConversationIdOrderByCreatedAtDesc(@Param("conversationId") String conversationId);

}
