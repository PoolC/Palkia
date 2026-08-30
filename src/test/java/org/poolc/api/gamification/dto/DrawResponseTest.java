package org.poolc.api.gamification.dto;

import org.junit.jupiter.api.Test;
import org.poolc.api.gamification.domain.CollectionDraw;
import org.poolc.api.gamification.domain.CollectibleCatalog;
import org.poolc.api.gamification.domain.CollectibleRarity;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DrawResponseTest {

    @Test
    void emitsDrawTimeWithAnExplicitKoreanTimezoneOffset() {
        CollectionDraw draw = mock(CollectionDraw.class);
        CollectibleCatalog collectible = mock(CollectibleCatalog.class);
        when(draw.getCollectible()).thenReturn(collectible);
        when(draw.getDrawnAt()).thenReturn(LocalDateTime.of(2026, 8, 30, 15, 0));
        when(draw.getRarityAtDraw()).thenReturn(CollectibleRarity.COMMON);

        DrawResponse response = new DrawResponse(draw);

        assertThat(response.getDrawnAt().getOffset().getId()).isEqualTo("+09:00");
    }
}
