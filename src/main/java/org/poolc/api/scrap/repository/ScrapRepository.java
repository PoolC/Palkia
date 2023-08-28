package org.poolc.api.scrap.repository;

import org.poolc.api.scrap.domain.Scrap;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long>, PagingAndSortingRepository<Scrap, Long> {
    boolean existsByMemberIdAndPostId(String memberId, Long postId);
    Optional<Scrap> findByMemberIdAndPostId(String memberId, Long postId);
    Page<Scrap> findAllByMemberId(String memberId, Pageable pageable);
}
