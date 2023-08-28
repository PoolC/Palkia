package org.poolc.api.badge.dto;

import lombok.Builder;
import lombok.Getter;
import org.poolc.api.badge.vo.BadgeWithOwn;

import java.util.List;

@Getter
@Builder
public class GetOtherBadgeResponse {
    private final List<BadgeWithOwn> data;
}
