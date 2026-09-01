package org.poolc.api.gamification.dto;

import lombok.Getter;
import org.poolc.api.gamification.domain.CollectibleCatalog;
import org.poolc.api.gamification.domain.CollectibleRarity;

@Getter
public class CollectionItemResponse {
    private final Long collectibleId;
    private final Long externalId;
    private final String name;
    private final Integer generation;
    private final String types;
    private final CollectibleRarity rarity;
    private final String spriteUrl;
    private final String shinySpriteUrl;
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
    private final boolean normalOwned;
    private final boolean shinyOwned;

    public CollectionItemResponse(CollectibleCatalog collectible, boolean normalOwned, boolean shinyOwned) {
        this.collectibleId = collectible.getId();
        this.externalId = collectible.getExternalId();
        this.name = collectible.getNameKo();
        this.generation = collectible.getGeneration();
        this.types = collectible.getTypes();
        this.rarity = collectible.getRarity();
        this.spriteUrl = collectible.getSpriteUrl();
        this.shinySpriteUrl = collectible.getShinySpriteUrl();
        this.category = collectible.getCategoryKo();
        this.description = collectible.getDescriptionKo();
        this.heightDecimeters = collectible.getHeightDecimeters();
        this.weightHectograms = collectible.getWeightHectograms();
        this.abilities = collectible.getAbilities();
        this.hp = collectible.getStatHp();
        this.attack = collectible.getStatAttack();
        this.defense = collectible.getStatDefense();
        this.specialAttack = collectible.getStatSpecialAttack();
        this.specialDefense = collectible.getStatSpecialDefense();
        this.speed = collectible.getStatSpeed();
        this.normalOwned = normalOwned;
        this.shinyOwned = shinyOwned;
    }
}
