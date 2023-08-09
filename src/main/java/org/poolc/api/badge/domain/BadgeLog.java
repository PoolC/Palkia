package org.poolc.api.badge.domain;

import lombok.Builder;
import lombok.Getter;
import org.poolc.api.member.domain.Member;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Table(name="BadgeLog")
public class BadgeLog {
    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "winTime", nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name="member", nullable = false,referencedColumnName = "uuid")
    private Member member;

    @ManyToOne
    @JoinColumn(name="badge",nullable = false,referencedColumnName = "id")
    private Badge badge;

    @Builder
    public BadgeLog(LocalDate date, Member member, Badge badge) {
        this.date = date;
        this.member = member;
        this.badge = badge;
    }

    protected BadgeLog() {

    }
}
