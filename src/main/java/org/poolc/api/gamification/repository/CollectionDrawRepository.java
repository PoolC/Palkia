package org.poolc.api.gamification.repository;

import org.poolc.api.gamification.domain.CollectionDraw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CollectionDrawRepository extends JpaRepository<CollectionDraw, Long> {
    @Query("select d from CollectionDraw d join fetch d.collectible where d.member.UUID = :memberUuid order by d.drawnAt desc")
    List<CollectionDraw> findAllByMemberUuidWithCollectible(@Param("memberUuid") String memberUuid);
}
