package org.poolc.api.badge.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateBadgeRequest {
    private final String name;
    private final String imageUrl;
    private final String description;
}
