package org.poolc.api.badge.dto;

import lombok.Builder;
import lombok.Getter;
import org.poolc.api.badge.domain.Badge;

import java.util.List;

@Getter
@Builder
public class GetAllBadgeResponse {
    private final List<Badge> data;
}
