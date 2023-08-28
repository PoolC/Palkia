package org.poolc.api.badge.vo;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class BadgeWithOwn {
    private final Long id;
    private final String name;
    private final String imageUrl;
    private final String description;
    private final LocalDate date;
    private final BadgeCategory category;
    private final boolean own;

    @Builder
    public BadgeWithOwn(Long id, String name, String imageUrl, String description, LocalDate date, BadgeCategory category) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.date = date;
        this.category = category;
        this.own = (date!=null);
    }
}
