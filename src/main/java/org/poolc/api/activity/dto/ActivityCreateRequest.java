package org.poolc.api.activity.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class ActivityCreateRequest {
    private final String title;
    private final String description;
    private final LocalDate startDate;
    private final Boolean isSeminar;
    private final String classHour;
    private final Long capacity;
    private final List<String> tags;

    @JsonCreator
    public ActivityCreateRequest(String title, String description, LocalDate startDate, Boolean isSeminar, String classHour, Long capacity, List<String> tags) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.isSeminar = isSeminar;
        this.classHour = classHour;
        this.capacity = capacity;
        this.tags = tags;
    }
}
