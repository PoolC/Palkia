package org.poolc.api.badge.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AssignBadgeRequest {
    private final String loginId;
    private final Long badgeId;
}
