package org.poolc.api.member.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class MyActivityDetailResponse {
    private final Long activityId;
    private final String title;
    private final BigDecimal recognizedHours;
    private final boolean hosted;
}
