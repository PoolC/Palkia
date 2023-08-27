package org.poolc.api.badge.repository;

import org.poolc.api.badge.domain.BadgeCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BadgeConditionRepository extends JpaRepository<BadgeCondition, Long> {
    @Query("select a from BadgeCondition a where a.uuid=(:uuid)")
    Optional<BadgeCondition> myAttendance(@Param("uuid") String uuid);
}
