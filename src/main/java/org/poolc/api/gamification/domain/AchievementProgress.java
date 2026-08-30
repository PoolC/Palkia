package org.poolc.api.gamification.domain;

import lombok.Getter;
import org.poolc.api.member.domain.Member;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "achievement_progress", uniqueConstraints = @UniqueConstraint(columnNames = {"member_uuid", "achievement_key", "period_key"}))
@SequenceGenerator(name = "ACHIEVEMENT_PROGRESS_SEQ", sequenceName = "ACHIEVEMENT_PROGRESS_SEQ")
public class AchievementProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACHIEVEMENT_PROGRESS_SEQ")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_uuid", nullable = false, referencedColumnName = "UUID")
    private Member member;

    @Column(name = "achievement_key", nullable = false, length = 60)
    private String achievementKey;

    @Column(name = "period_key", nullable = false, length = 30)
    private String periodKey;

    @Column(nullable = false)
    private int progress;

    @Column(nullable = false)
    private boolean claimed;

    @Column(name = "claimed_count", nullable = false, columnDefinition = "integer default 0")
    private int claimedCount;

    protected AchievementProgress() {
    }

    public AchievementProgress(Member member, String achievementKey, String periodKey, int progress) {
        this.member = member;
        this.achievementKey = achievementKey;
        this.periodKey = periodKey;
        this.progress = progress;
    }

    public void updateProgress(int progress) {
        this.progress = Math.max(this.progress, progress);
    }

    public void claim() {
        this.claimed = true;
    }

    public void claim(int count) {
        this.claimedCount += count;
    }
}
