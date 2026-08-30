package org.poolc.api.gamification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CatalogSyncWorker {
    private final CatalogSyncService catalogSyncService;

    @Async("catalogSyncExecutor")
    public void synchronize(Long runId) {
        catalogSyncService.synchronize(runId);
    }
}
