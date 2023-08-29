package org.poolc.api.badge.repository;

import org.poolc.api.badge.domain.Badge;
import org.poolc.api.badge.vo.BadgeWithCount;
import org.poolc.api.badge.vo.BadgeWithOwn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BadgeRepository extends JpaRepository<Badge,Long> {

    @Query("select b from Badge b where b.id=(:id)")
    Optional<Badge> findBadgeById(@Param("id") Long id);

    @Query("select b from Badge b order by b.id")
    List<Badge> findAllBadge();

    @Query("select b from Badge b where b.name=(:name)")
    Optional<Badge> findBadgeByName(@Param("name") String name);

    @Query("select new org.poolc.api.badge.vo.BadgeWithOwn(b.id, b.name, b.imageUrl, b.description,l.date, b.category) from Badge b left outer join BadgeLog l on b.id = l.badge.id and l.member.UUID = (:uuid) order by b.id")
    List<BadgeWithOwn> findAllBadgeWithOwn(@Param("uuid") String uuid);

    @Query("select new org.poolc.api.badge.vo.BadgeWithCount(b.id, count(l))from Badge b left outer join BadgeLog l on b.id=l.badge.id group by b.id")
    List<BadgeWithCount> findAllBadgeWithCount();
}
