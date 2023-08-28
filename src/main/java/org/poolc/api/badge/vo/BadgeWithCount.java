package org.poolc.api.badge.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BadgeWithCount {
    private final Long id;
    private final Long count;

    public BadgeWithCount(Long id, Long count) {
        this.id = id;
        this.count = count;
    }
}
