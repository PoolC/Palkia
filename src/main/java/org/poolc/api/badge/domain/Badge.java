package org.poolc.api.badge.domain;

import lombok.Builder;
import lombok.Getter;
import org.poolc.api.badge.vo.BadgeCategory;

import javax.persistence.*;

@Entity
@Getter
@SequenceGenerator(
        name= "BADGE_SEQ_GENERATOR",
        sequenceName = "BADGE_SEQ",
        initialValue = 100
)
@Table(name = "Badge")
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BADGE_SEQ_GENERATOR")
    @Column(name="id",unique = true,nullable = false)
    private Long id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name="image_url", nullable = false)
    private String imageUrl;

    @Column(name="category")
    private BadgeCategory category;

    @Builder
    public Badge(Long id, String name, String description, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.category = BadgeCategory.ETC;
    }

    public void updateBadge(String name, String description, String imageUrl){
        this.name = name;
        this.imageUrl = imageUrl;
        this.description=description;
    }

    protected Badge() {

    }
}
