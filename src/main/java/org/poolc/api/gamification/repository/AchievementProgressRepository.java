package org.poolc.api.gamification.repository;

import org.poolc.api.gamification.domain.AchievementProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface AchievementProgressRepository extends JpaRepository<AchievementProgress, Long> {
    @Query("select p from AchievementProgress p where p.member.UUID = :memberUuid and p.achievementKey = :achievementKey and p.periodKey = :periodKey")
    Optional<AchievementProgress> findByMemberUuidAndAchievementKeyAndPeriodKey(
            @Param("memberUuid") String memberUuid,
            @Param("achievementKey") String achievementKey,
            @Param("periodKey") String periodKey);

    @Query("select p from AchievementProgress p where p.member.UUID = :memberUuid and p.achievementKey = :achievementKey")
    List<AchievementProgress> findAllByMemberUuidAndAchievementKey(
            @Param("memberUuid") String memberUuid,
            @Param("achievementKey") String achievementKey);
}
