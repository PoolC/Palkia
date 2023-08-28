package org.poolc.api.badge.dto;

import lombok.Builder;
import lombok.Getter;
import org.poolc.api.badge.vo.BadgeWithOwn;
import org.poolc.api.badge.vo.BadgeWithOwnCount;

import java.util.List;

@Getter
@Builder
public class GetAllBadgeResponse {
    private final List<BadgeWithOwnCount> data;
}
