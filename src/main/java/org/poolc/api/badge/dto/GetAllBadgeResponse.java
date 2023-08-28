package org.poolc.api.badge.dto;

import lombok.Builder;
import lombok.Getter;
import org.poolc.api.badge.domain.BadgeCondition;
import org.poolc.api.badge.vo.BadgeWithOwn;
import org.poolc.api.badge.vo.BadgeWithOwnCount;

import java.util.List;

@Getter
public class GetAllBadgeResponse {
    private final List<BadgeWithOwnCount> data;
    private final Long attendance;
    private final Long baekjoon;
    private final Long bronze;
    private final Long silver;
    private final Long gold;
    private final Long platinum;
    private final Long diamond;
    private final Long ruby;

    @Builder
    public GetAllBadgeResponse(BadgeCondition condition,List<BadgeWithOwnCount> data){
        this.data = data;
        this.attendance = condition.getAttendance();
        this.baekjoon = condition.getBaekjoon();
        this.bronze = condition.getBronzeCount();
        this.silver = condition.getSilverCount();
        this.gold = condition.getGoldCount();
        this.platinum = condition.getPlatinumCount();
        this.diamond = condition.getDiamondCount();
        this.ruby = condition.getRubyCount();
    }
}
