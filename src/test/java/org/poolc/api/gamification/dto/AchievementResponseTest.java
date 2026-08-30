package org.poolc.api.gamification.dto;

import org.junit.jupiter.api.Test;
import org.poolc.api.gamification.domain.AchievementProgress;
import org.poolc.api.member.domain.Member;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

class AchievementResponseTest {

    @Test
    void repeatableQuestExposesOnlyWholeUnclaimedRewardBatches() {
        AchievementProgress progress = new AchievementProgress(Mockito.mock(Member.class), "REPEAT_DRAW", "REPEATABLE-2026-2", 6);
        progress.claim(1);

        AchievementResponse response = new AchievementResponse("REPEAT_DRAW", "REPEATABLE", "title", "description",
                3, progress, "NORMAL", 1);

        assertThat(response.getClaimableCount()).isEqualTo(1);
    }

    @Test
    void regularQuestHasAtMostOneClaimableReward() {
        AchievementProgress progress = new AchievementProgress(Mockito.mock(Member.class), "SEASON_ACTIVITY_5", "2026-2", 12);

        AchievementResponse response = new AchievementResponse("SEASON_ACTIVITY_5", "SEASON", "title", "description",
                5, progress, "NORMAL", 1);

        assertThat(response.getClaimableCount()).isEqualTo(1);
    }
}
