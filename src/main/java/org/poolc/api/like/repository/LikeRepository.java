package org.poolc.api.like.repository;

import org.poolc.api.like.domain.Like;
import org.poolc.api.like.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByMemberIdAndSubjectAndSubjectId(String memberId, Subject subject, Long subjectId);
    boolean existsByMemberIdAndSubjectAndSubjectId(String memberId, Subject subject, Long subjectId);
}
