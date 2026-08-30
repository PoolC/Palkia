package org.poolc.api.gamification.dto;

import lombok.Getter;
import org.poolc.api.gamification.domain.AchievementProgress;

@Getter
public class AchievementResponse {
    private final String key;
    private final String type;
    private final String title;
    private final String description;
    private final int target;
    private final int progress;
    private final String rewardBallType;
    private final int rewardAmount;
    private final boolean claimed;
    private final int claimedCount;
    private final int claimableCount;

    public AchievementResponse(String key, String type, String title, String description,
                               int target, AchievementProgress progress, String rewardBallType, int rewardAmount) {
        this.key = key;
        this.type = type;
        this.title = title;
        this.description = description;
        this.target = target;
        this.progress = progress.getProgress();
        this.rewardBallType = rewardBallType;
        this.rewardAmount = rewardAmount;
        this.claimed = progress.isClaimed();
        this.claimedCount = progress.getClaimedCount();
        int completedRewardCount = "REPEATABLE".equals(type)
                ? progress.getProgress() / target
                : progress.getProgress() >= target ? 1 : 0;
        this.claimableCount = Math.max(0, completedRewardCount - progress.getClaimedCount());
    }
}
