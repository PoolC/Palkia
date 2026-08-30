package org.poolc.api.gamification.repository;

import org.poolc.api.gamification.domain.MemberFeaturedCollectible;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberFeaturedCollectibleRepository extends JpaRepository<MemberFeaturedCollectible, Long> {
    @Query("select f from MemberFeaturedCollectible f join fetch f.collectible where f.member.UUID = :memberUuid")
    Optional<MemberFeaturedCollectible> findByMemberUuid(@Param("memberUuid") String memberUuid);

    @Query("select f from MemberFeaturedCollectible f join fetch f.member join fetch f.collectible where f.member.UUID in :memberUuids")
    List<MemberFeaturedCollectible> findAllByMemberUuidIn(@Param("memberUuids") Collection<String> memberUuids);
}
