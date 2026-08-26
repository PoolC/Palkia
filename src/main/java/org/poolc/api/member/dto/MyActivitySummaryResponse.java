package org.poolc.api.member.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class MyActivitySummaryResponse {
    private final BigDecimal totalHours;
    private final BigDecimal seminarStudyHours;
    private final BigDecimal officialActivityHours;
    private final BigDecimal projectHours;
    private final List<MyActivityDetailResponse> seminarStudyActivities;
    private final List<MyActivityDetailResponse> officialActivities;
}
