package org.poolc.api.baekjoon.domain;

import lombok.Builder;
import lombok.Getter;
import org.poolc.api.member.domain.Member;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Table(name = "Baekjoon")
public class Baekjoon {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "date", nullable=false)
    private LocalDate date;

    @Column(name = "language", nullable = false)
    private String language;

    @Column(name = "submission_id", nullable = false)
    private Long submissionId;

    @ManyToOne
    @JoinColumn(name = "member", nullable = false, referencedColumnName = "uuid")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "problemId", nullable = false, referencedColumnName = "id")
    private Problem problem;

    protected Baekjoon() {

    }
    @Builder
    public Baekjoon(LocalDate date, String language, Long submissionId, Member member, Problem problem) {
        this.date = date;
        this.language = language;
        this.submissionId = submissionId;
        this.member = member;
        this.problem = problem;
    }
}
