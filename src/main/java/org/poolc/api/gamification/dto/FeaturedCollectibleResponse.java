package org.poolc.api.gamification.dto;

import lombok.Getter;
import org.poolc.api.gamification.domain.CollectibleCatalog;
import org.poolc.api.gamification.domain.MemberFeaturedCollectible;

@Getter
public class FeaturedCollectibleResponse {
    private final Long collectibleId;
    private final Long externalId;
    private final String name;
    private final String spriteUrl;
    private final String shinySpriteUrl;
    private final boolean shiny;
    private final boolean useAsProfile;

    public FeaturedCollectibleResponse(MemberFeaturedCollectible featured) {
        CollectibleCatalog collectible = featured.getCollectible();
        this.collectibleId = collectible.getId();
        this.externalId = collectible.getExternalId();
        this.name = collectible.getNameKo();
        this.spriteUrl = collectible.getSpriteUrl();
        this.shinySpriteUrl = collectible.getShinySpriteUrl();
        this.shiny = featured.isShiny();
        this.useAsProfile = featured.isUseAsProfile();
    }
}
