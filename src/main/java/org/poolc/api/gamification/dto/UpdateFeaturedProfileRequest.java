package org.poolc.api.gamification.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class UpdateFeaturedProfileRequest {
    private final boolean useAsProfile;

    @JsonCreator
    public UpdateFeaturedProfileRequest(boolean useAsProfile) {
        this.useAsProfile = useAsProfile;
    }
}
