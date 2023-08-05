package org.poolc.api.badge.repository;

import org.poolc.api.badge.domain.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BadgeRepository extends JpaRepository<Badge,Long> {

    @Query("select b from Badge b where b.id=(:id)")
    Optional<Badge> findBadgeById(@Param("id") Long id);

    @Query("select b from Badge b")
    List<Badge> findAllBadge();

    @Query("select b from Badge b where b.name=(:name)")
    Optional<Badge> findBadgeByName(@Param("name") String name);
}
