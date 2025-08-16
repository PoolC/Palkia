package org.poolc.api.kubernetes.dto;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class PostKubernetesRequestDto {
    private final Map<String,String> activeMembersMap;

    public PostKubernetesRequestDto(Map<String,String> activeMembersMap) {
        this.activeMembersMap = activeMembersMap;
    }
}
