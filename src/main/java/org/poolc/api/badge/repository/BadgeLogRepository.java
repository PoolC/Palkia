package org.poolc.api.badge.repository;

import org.poolc.api.badge.domain.BadgeLog;
import org.poolc.api.badge.vo.MyBadgeSearchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BadgeLogRepository extends JpaRepository<BadgeLog,Long> {

    @Query("select new org.poolc.api.badge.vo.MyBadgeSearchResult(b.id,b.name,b.imageUrl, b.description,l.date) from BadgeLog l inner join Badge b on l.badge.id=b.id where l.member.UUID=(:uuid)")
    List<MyBadgeSearchResult> findMyBadge(@Param("uuid") String uuid);

    @Query("select count(l) from BadgeLog l where l.member.UUID=(:uuid) and l.badge.id=(:badgeId)")
    Long myBadgeCheck(@Param("uuid") String uuid, @Param("badgeId") Long badgeId);
}
