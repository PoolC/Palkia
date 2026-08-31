package org.poolc.api.gamification.dto;

import lombok.Getter;

@Getter
public class GameSummaryResponse {
    private final BallBalancesResponse ballBalances;
    private final long totalCatalogCount;
    private final long collectedCatalogCount;
    private final long shinyCatalogCount;
    private final long normalCatalogCount;
    private final long totalVariantCount;
    private final long collectedVariantCount;
    private final ShinyDrawStatus shinyDrawStatus;

    public GameSummaryResponse(
            BallBalancesResponse ballBalances,
            long totalCatalogCount,
            long collectedCatalogCount,
            long shinyCatalogCount,
            long normalCatalogCount,
            long totalVariantCount,
            long collectedVariantCount,
            ShinyDrawStatus shinyDrawStatus) {
        this.ballBalances = ballBalances;
        this.totalCatalogCount = totalCatalogCount;
        this.collectedCatalogCount = collectedCatalogCount;
        this.shinyCatalogCount = shinyCatalogCount;
        this.normalCatalogCount = normalCatalogCount;
        this.totalVariantCount = totalVariantCount;
        this.collectedVariantCount = collectedVariantCount;
        this.shinyDrawStatus = shinyDrawStatus;
    }
}
