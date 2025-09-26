package org.poolc.api.kubernetes.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class GetMyKubernetesKeyResponseDto {

    private final String key;

    @JsonCreator
    public GetMyKubernetesKeyResponseDto(String key){
        this.key = key;
    }
}
