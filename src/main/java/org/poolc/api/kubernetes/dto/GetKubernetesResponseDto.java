package org.poolc.api.kubernetes.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.List;

@Getter
public class GetKubernetesResponseDto {
    private final List<String> activeMembers;

    @JsonCreator
    public GetKubernetesResponseDto(List<String> activeMembers) {
        this.activeMembers = activeMembers;
    }
}
