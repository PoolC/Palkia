package org.poolc.api.baekjoon.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class BaekjoonResponse {
    private final LocalDate date;
    private final Long problemId;
    private final String level;

    public BaekjoonResponse(LocalDate date, Long problemId, String level) {
        this.date = date;
        this.problemId = problemId;
        this.level = level;
    }
}
