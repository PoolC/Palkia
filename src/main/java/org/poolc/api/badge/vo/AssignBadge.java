package org.poolc.api.badge.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AssignBadge {
    private Long id;
    private Boolean own;

    @Builder
    public AssignBadge(Long id, Boolean own){
        this.id=id;
        this.own =own;
    }
}
