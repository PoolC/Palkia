package org.poolc.api.scrap.repository;

import org.poolc.api.scrap.domain.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    boolean existsByMemberIdAndPostId(String memberId, Long postId);
    Optional<Scrap> findByMemberIdAndPostId(String memberId, Long postId);
    List<Scrap> findAllByMemberId(String memberId);
}
