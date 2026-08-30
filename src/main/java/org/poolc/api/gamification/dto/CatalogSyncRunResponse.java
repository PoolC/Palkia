package org.poolc.api.gamification.dto;

import lombok.Getter;
import org.poolc.api.gamification.domain.CatalogSyncRun;
import org.poolc.api.gamification.domain.SyncStatus;

import java.time.LocalDateTime;

@Getter
public class CatalogSyncRunResponse {
    private final Long id;
    private final SyncStatus status;
    private final int processedCount;
    private final LocalDateTime startedAt;
    private final LocalDateTime completedAt;
    private final String message;

    public CatalogSyncRunResponse(CatalogSyncRun run) {
        this.id = run.getId();
        this.status = run.getStatus();
        this.processedCount = run.getProcessedCount();
        this.startedAt = run.getStartedAt();
        this.completedAt = run.getCompletedAt();
        this.message = run.getMessage();
    }
}
