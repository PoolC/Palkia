package org.poolc.api.gamification.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "collectible_catalog", uniqueConstraints = @UniqueConstraint(columnNames = "external_id"))
@SequenceGenerator(name = "COLLECTIBLE_CATALOG_SEQ", sequenceName = "COLLECTIBLE_CATALOG_SEQ")
public class CollectibleCatalog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COLLECTIBLE_CATALOG_SEQ")
    private Long id;

    @Column(name = "external_id", nullable = false)
    private Long externalId;

    @Column(nullable = false, length = 120)
    private String slug;

    @Column(name = "name_ko", nullable = false, length = 120)
    private String nameKo;

    @Column(nullable = false)
    private Integer generation;

    @Column(nullable = false, length = 255)
    private String types;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CollectibleRarity rarity;

    @Column(name = "sprite_url", length = 1024)
    private String spriteUrl;

    @Column(name = "shiny_sprite_url", length = 1024)
    private String shinySpriteUrl;

    @Column(name = "category_ko", length = 120)
    private String categoryKo;

    @Column(name = "description_ko", columnDefinition = "text")
    private String descriptionKo;

    @Column(name = "height_decimeters")
    private Integer heightDecimeters;

    @Column(name = "weight_hectograms")
    private Integer weightHectograms;

    @Column(length = 255)
    private String abilities;

    @Column(name = "stat_hp")
    private Integer statHp;

    @Column(name = "stat_attack")
    private Integer statAttack;

    @Column(name = "stat_defense")
    private Integer statDefense;

    @Column(name = "stat_special_attack")
    private Integer statSpecialAttack;

    @Column(name = "stat_special_defense")
    private Integer statSpecialDefense;

    @Column(name = "stat_speed")
    private Integer statSpeed;

    @Column(nullable = false)
    private boolean enabled;

    @Column(name = "imported_at", nullable = false)
    private LocalDateTime importedAt;

    protected CollectibleCatalog() {
    }

    public CollectibleCatalog(Long externalId, String slug, String nameKo, Integer generation, String types,
                              CollectibleRarity rarity, String spriteUrl, String shinySpriteUrl, String categoryKo, String descriptionKo,
                              Integer heightDecimeters, Integer weightHectograms, String abilities,
                              Integer statHp, Integer statAttack, Integer statDefense,
                              Integer statSpecialAttack, Integer statSpecialDefense, Integer statSpeed) {
        this.externalId = externalId;
        this.slug = slug;
        this.nameKo = nameKo;
        this.generation = generation;
        this.types = types;
        this.rarity = rarity;
        this.spriteUrl = spriteUrl;
        this.shinySpriteUrl = shinySpriteUrl;
        this.categoryKo = categoryKo;
        this.descriptionKo = descriptionKo;
        this.heightDecimeters = heightDecimeters;
        this.weightHectograms = weightHectograms;
        this.abilities = abilities;
        this.statHp = statHp;
        this.statAttack = statAttack;
        this.statDefense = statDefense;
        this.statSpecialAttack = statSpecialAttack;
        this.statSpecialDefense = statSpecialDefense;
        this.statSpeed = statSpeed;
        this.enabled = true;
        this.importedAt = LocalDateTime.now();
    }

    public void updateFromImport(String slug, String nameKo, Integer generation, String types,
                                 CollectibleRarity rarity, String spriteUrl, String shinySpriteUrl, String categoryKo, String descriptionKo,
                                 Integer heightDecimeters, Integer weightHectograms, String abilities,
                                 Integer statHp, Integer statAttack, Integer statDefense,
                                 Integer statSpecialAttack, Integer statSpecialDefense, Integer statSpeed) {
        this.slug = slug;
        this.nameKo = nameKo;
        this.generation = generation;
        this.types = types;
        this.rarity = rarity;
        this.spriteUrl = spriteUrl;
        this.shinySpriteUrl = shinySpriteUrl;
        this.categoryKo = categoryKo;
        this.descriptionKo = descriptionKo;
        this.heightDecimeters = heightDecimeters;
        this.weightHectograms = weightHectograms;
        this.abilities = abilities;
        this.statHp = statHp;
        this.statAttack = statAttack;
        this.statDefense = statDefense;
        this.statSpecialAttack = statSpecialAttack;
        this.statSpecialDefense = statSpecialDefense;
        this.statSpeed = statSpeed;
        this.importedAt = LocalDateTime.now();
    }
}
