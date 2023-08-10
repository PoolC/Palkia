package org.poolc.api.baekjoon.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetMyBaekjoonResponse {
    private final List<BaekjoonResponse> data;
}
