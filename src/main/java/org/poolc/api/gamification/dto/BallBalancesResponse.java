package org.poolc.api.gamification.dto;

import lombok.Getter;
@Getter
public class BallBalancesResponse {
    private final long normal;

    public BallBalancesResponse(long normal) {
        this.normal = normal;
    }
}
