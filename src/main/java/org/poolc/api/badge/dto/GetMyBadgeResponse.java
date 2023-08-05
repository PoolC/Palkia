package org.poolc.api.badge.dto;

import lombok.Builder;
import lombok.Getter;
import org.poolc.api.badge.vo.MyBadgeSearchResult;

import java.util.List;

@Getter
@Builder
public class GetMyBadgeResponse {
    private final List<MyBadgeSearchResult> data;
}
