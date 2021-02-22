package org.poolc.api.domain;

import lombok.Getter;
import lombok.Setter;
import org.poolc.api.common.domain.TimestampEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "Project")
public class Project extends TimestampEntity {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private long id;

    @Column(name = "name", columnDefinition = "varchar(255)", nullable = false)
    private String name;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "genre", columnDefinition = "varchar(255)", nullable = false)
    private String genre;

    @Column(name = "duration", columnDefinition = "varchar(255)", nullable = false)
    private String duration;

    @Column(name = "thumbnailURL", columnDefinition = "varchar(255)", nullable = false)
    private String thumbnailURL;

    @Lob
    @Column(name = "body", nullable = false)
    private String body;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    List<ProjectMember> members = new ArrayList<>();
}