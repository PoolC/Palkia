package org.poolc.api.domain;

import lombok.Getter;
import org.poolc.api.member.domain.Member;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "ActivityMember")
@Getter
public class ActivityMember {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "activityID", referencedColumnName = "ID", nullable = false)
    private Activity activity;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "memberID", referencedColumnName = "UUID", nullable = false)
    private Member member;
}