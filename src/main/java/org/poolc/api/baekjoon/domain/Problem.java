package org.poolc.api.baekjoon.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@Table(name = "problem")
public class Problem {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "problem_id", unique = true)
    private Long problemId;

    @Column(name = "title")
    private String title;

    @Column(name = "level")
    private String level;

    @ElementCollection
    @CollectionTable(name = "problem_tags",joinColumns = @JoinColumn(name = "id", referencedColumnName = "id"))
    private List<String> tags;

    @Builder
    public Problem(Long problemId, String title, String level, List<String> tags) {
        this.problemId = problemId;
        this.title = title;
        this.level = level;
        this.tags = tags;
    }

    public Problem(Long id, Long problemId, String title, String level, List<String> tags) {
        this.id = id;
        this.problemId = problemId;
        this.title = title;
        this.level = level;
        this.tags = tags;
    }

    public Problem(){}
}
