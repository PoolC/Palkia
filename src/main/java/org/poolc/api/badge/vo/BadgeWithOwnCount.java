package org.poolc.api.badge.vo;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class BadgeWithOwnCount {
    private final Long id;
    private final String name;
    private final String imageUrl;
    private final String description;
    private final LocalDate date;
    private final BadgeCategory category;
    private Long count;
    private final boolean own;

    @Builder
    public BadgeWithOwnCount(BadgeWithOwn badgeWithOwn) {
        this.id = badgeWithOwn.getId();
        this.name = badgeWithOwn.getName();
        this.imageUrl = badgeWithOwn.getImageUrl();
        this.description = badgeWithOwn.getDescription();
        this.date = badgeWithOwn.getDate();
        this.category = badgeWithOwn.getCategory();
        this.own = badgeWithOwn.isOwn();
    }

    public void setCount(Long count){
        this.count=count;
    }
}
