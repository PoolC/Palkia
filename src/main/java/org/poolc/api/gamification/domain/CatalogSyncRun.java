package org.poolc.api.gamification.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "catalog_sync_run")
@SequenceGenerator(name = "CATALOG_SYNC_RUN_SEQ", sequenceName = "CATALOG_SYNC_RUN_SEQ")
public class CatalogSyncRun {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CATALOG_SYNC_RUN_SEQ")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SyncStatus status;

    @Column(name = "processed_count", nullable = false)
    private int processedCount;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(length = 1024)
    private String message;

    public CatalogSyncRun() {
        this.status = SyncStatus.RUNNING;
        this.startedAt = LocalDateTime.now();
    }

    public void complete(int processedCount) {
        this.status = SyncStatus.COMPLETED;
        this.processedCount = processedCount;
        this.completedAt = LocalDateTime.now();
        this.message = null;
    }

    public void fail(String message) {
        this.status = SyncStatus.FAILED;
        this.completedAt = LocalDateTime.now();
        this.message = message == null ? "동기화에 실패했습니다." : message.substring(0, Math.min(message.length(), 1024));
    }
}
