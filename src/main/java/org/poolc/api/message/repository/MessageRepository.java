package org.poolc.api.message.repository;

import java.util.Optional;
import org.poolc.api.message.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("select m from Message m where m.conversation.id=(:id)")
    List<Message> findAllByConversationID(@Param("id") String conversationId);

    @Query("SELECT m FROM Message m WHERE m.conversation.id = :conversationId ORDER BY m.createdAt DESC")
    Optional<Message> findTopByConversationIdOrderByCreatedAtDesc(@Param("conversationId") String conversationId);

}
