package org.poolc.api.gamification.dto;

import lombok.Getter;

@Getter
public class GameSummaryResponse {
    private final BallBalancesResponse ballBalances;
    private final long totalCatalogCount;
    private final long collectedCatalogCount;
    private final long shinyCatalogCount;

    public GameSummaryResponse(BallBalancesResponse ballBalances, long totalCatalogCount, long collectedCatalogCount, long shinyCatalogCount) {
        this.ballBalances = ballBalances;
        this.totalCatalogCount = totalCatalogCount;
        this.collectedCatalogCount = collectedCatalogCount;
        this.shinyCatalogCount = shinyCatalogCount;
    }
}
