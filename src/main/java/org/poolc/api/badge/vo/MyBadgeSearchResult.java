package org.poolc.api.badge.vo;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class MyBadgeSearchResult {
    private final Long id;
    private final String name;
    private final String imageUrl;
    private final String description;
    private final LocalDate date;

    public MyBadgeSearchResult(Long id, String name, String imageUrl, String description, LocalDate date) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.date = date;
    }
}
