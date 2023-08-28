package org.poolc.api.badge.dto;

import lombok.Builder;
import lombok.Getter;
import org.poolc.api.badge.vo.AssignBadge;

import java.util.List;

@Getter
@Builder
public class AssignBadgeRequest {
    private List<AssignBadge> data;

    @Builder
    public AssignBadgeRequest(List<AssignBadge> data) {
        this.data = data;
    }

    public AssignBadgeRequest() {
    }
}
