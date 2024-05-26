package org.poolc.api.conversation.repository;

import org.poolc.api.conversation.domain.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Optional<Conversation> findByStarterLoginIDAndOtherLoginIDAndStarterAnonymousFalseAndOtherAnonymousFalse(String starterLoginID, String otherLoginID);
    Optional<Conversation> findById(String id);
}
