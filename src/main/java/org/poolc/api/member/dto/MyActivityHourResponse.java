package org.poolc.api.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyActivityHourResponse {
    private final Long hour;
}
