package org.poolc.api.gamification.repository;

import org.poolc.api.gamification.domain.CatalogSyncRun;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CatalogSyncRunRepository extends JpaRepository<CatalogSyncRun, Long> {
    Optional<CatalogSyncRun> findTopByOrderByStartedAtDesc();
}
