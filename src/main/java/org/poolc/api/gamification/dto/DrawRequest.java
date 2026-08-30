package org.poolc.api.gamification.dto;

import lombok.Getter;
import org.poolc.api.gamification.domain.BallType;

@Getter
public class DrawRequest {
    private BallType ballType;
}
