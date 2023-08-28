package org.poolc.api.baekjoon.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class BaekjoonResponse {
    private final LocalDate date;
    private final Long problemId;

    public BaekjoonResponse(LocalDate date, Long problemId) {
        this.date = date;
        this.problemId = problemId;
    }
}
