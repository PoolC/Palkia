package org.poolc.api.gamification.domain;

import lombok.Getter;
import org.poolc.api.member.domain.Member;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "member_featured_collectible", uniqueConstraints = @UniqueConstraint(columnNames = "member_uuid"))
@SequenceGenerator(name = "MEMBER_FEATURED_COLLECTIBLE_SEQ", sequenceName = "MEMBER_FEATURED_COLLECTIBLE_SEQ")
public class MemberFeaturedCollectible {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_FEATURED_COLLECTIBLE_SEQ")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_uuid", nullable = false, referencedColumnName = "UUID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collectible_id", nullable = false)
    private CollectibleCatalog collectible;

    @Column(nullable = false)
    private boolean shiny;

    @Column(name = "use_as_profile", nullable = false)
    private boolean useAsProfile;

    protected MemberFeaturedCollectible() {
    }

    public MemberFeaturedCollectible(Member member, CollectibleCatalog collectible, boolean shiny) {
        this.member = member;
        this.collectible = collectible;
        this.shiny = shiny;
        this.useAsProfile = false;
    }

    public void updateCollectible(CollectibleCatalog collectible, boolean shiny) {
        this.collectible = collectible;
        this.shiny = shiny;
    }

    public void updateUseAsProfile(boolean useAsProfile) {
        this.useAsProfile = useAsProfile;
    }
}
