package org.poolc.api.gamification.domain;

import lombok.Getter;
import org.poolc.api.member.domain.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(
        name = "collection_draw",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_collection_draw_member_collectible_variant",
                columnNames = {"member_uuid", "collectible_id", "shiny"}))
@SequenceGenerator(name = "COLLECTION_DRAW_SEQ", sequenceName = "COLLECTION_DRAW_SEQ")
public class CollectionDraw {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COLLECTION_DRAW_SEQ")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_uuid", nullable = false, referencedColumnName = "UUID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collectible_id", nullable = false)
    private CollectibleCatalog collectible;

    @Enumerated(EnumType.STRING)
    @Column(name = "rarity_at_draw", nullable = false, length = 20)
    private CollectibleRarity rarityAtDraw;

    @Column(nullable = false)
    private boolean shiny;

    @Column(name = "drawn_at", nullable = false)
    private LocalDateTime drawnAt;

    protected CollectionDraw() {
    }

    public CollectionDraw(Member member, CollectibleCatalog collectible, boolean shiny) {
        this.member = member;
        this.collectible = collectible;
        this.rarityAtDraw = collectible.getRarity();
        this.shiny = shiny;
        this.drawnAt = LocalDateTime.now();
    }
}
