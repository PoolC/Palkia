package org.poolc.api.badge.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostBadgeRequest {
    private final String name;
    private final String description;
    private final String imageUrl;
}
