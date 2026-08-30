package org.poolc.api.gamification.dto;

import lombok.Getter;
import org.poolc.api.gamification.domain.CollectionDraw;
import org.poolc.api.gamification.domain.CollectibleRarity;

import java.time.OffsetDateTime;
import java.time.ZoneId;

@Getter
public class DrawResponse {
    private final Long drawId;
    private final Long collectibleId;
    private final String name;
    private final String spriteUrl;
    private final Long externalId;
    private final String category;
    private final String description;
    private final Integer heightDecimeters;
    private final Integer weightHectograms;
    private final String abilities;
    private final Integer hp;
    private final Integer attack;
    private final Integer defense;
    private final Integer specialAttack;
    private final Integer specialDefense;
    private final Integer speed;
    private final CollectibleRarity rarity;
    private final boolean shiny;
    private final OffsetDateTime drawnAt;
    private final BallBalancesResponse ballBalances;

    public DrawResponse(CollectionDraw draw) {
        this(draw, null);
    }

    public DrawResponse(CollectionDraw draw, BallBalancesResponse ballBalances) {
        this.drawId = draw.getId();
        this.collectibleId = draw.getCollectible().getId();
        this.name = draw.getCollectible().getNameKo();
        this.spriteUrl = draw.isShiny() && draw.getCollectible().getShinySpriteUrl() != null
                ? draw.getCollectible().getShinySpriteUrl()
                : draw.getCollectible().getSpriteUrl();
        this.externalId = draw.getCollectible().getExternalId();
        this.category = draw.getCollectible().getCategoryKo();
        this.description = draw.getCollectible().getDescriptionKo();
        this.heightDecimeters = draw.getCollectible().getHeightDecimeters();
        this.weightHectograms = draw.getCollectible().getWeightHectograms();
        this.abilities = draw.getCollectible().getAbilities();
        this.hp = draw.getCollectible().getStatHp();
        this.attack = draw.getCollectible().getStatAttack();
        this.defense = draw.getCollectible().getStatDefense();
        this.specialAttack = draw.getCollectible().getStatSpecialAttack();
        this.specialDefense = draw.getCollectible().getStatSpecialDefense();
        this.speed = draw.getCollectible().getStatSpeed();
        this.rarity = draw.getRarityAtDraw();
        this.shiny = draw.isShiny();
        this.drawnAt = draw.getDrawnAt().atZone(ZoneId.of("Asia/Seoul")).toOffsetDateTime();
        this.ballBalances = ballBalances;
    }
}
