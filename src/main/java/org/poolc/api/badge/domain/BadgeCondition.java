package org.poolc.api.badge.domain;

import lombok.Builder;
import lombok.Getter;
import org.poolc.api.member.domain.Member;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Table(name = "BadgeCondition")
public class BadgeCondition {
    @Id
    @Column(name="uuid",unique = true, nullable=false)
    private String uuid;

    @Column(name = "attendance")
    private Long attendance = 1L;

    @Column(name = "last_attendance")
    private LocalDate lastAttendance;

    @Builder
    public BadgeCondition(Member member){
        this.uuid=member.getUUID();
        this.attendance=1L;
        this.lastAttendance=LocalDate.now();
    }

    public void addAttendance(){
        this.lastAttendance = LocalDate.now();
        this.attendance++;
    };

    protected BadgeCondition(){}
}
