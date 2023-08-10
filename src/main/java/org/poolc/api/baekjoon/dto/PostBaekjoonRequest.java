package org.poolc.api.baekjoon.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PostBaekjoonRequest {
    private final Long problemId;
    private final Long submissionId;
    private final String title;
    private final String level;
    private final List<String> problemTags;
    private final String language;
}
