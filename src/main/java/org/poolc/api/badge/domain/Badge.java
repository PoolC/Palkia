package org.poolc.api.badge.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "Badge")
public class Badge {
    @Id
    @GeneratedValue
    @Column(name="id",unique = true,nullable = false)
    private Long id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name="image_url", nullable = false)
    private String imageUrl;

    @Builder
    public Badge(Long id, String name, String description, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public void updateBadge(String name, String description, String imageUrl){
        this.name = name;
        this.imageUrl = imageUrl;
        this.description=description;
    }

    protected Badge() {

    }
}
