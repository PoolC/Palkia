package org.poolc.api.gamification.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class UpdateFeaturedCollectibleRequest {
    private final Long collectibleId;
    private final boolean shiny;

    @JsonCreator
    public UpdateFeaturedCollectibleRequest(Long collectibleId, boolean shiny) {
        this.collectibleId = collectibleId;
        this.shiny = shiny;
    }
}
