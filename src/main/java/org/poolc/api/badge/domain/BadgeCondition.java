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
    private Long attendance;

    @Column(name = "last_attendance")
    private LocalDate lastAttendance;

    @Column(name = "baekjoon")
    private Long baekjoon;

    @Column(name = "last_baekjoon")
    private LocalDate lastBaekjoon;

    @Column(name = "bronze_count")
    private Long bronzeCount;

    @Column(name = "silver_count")
    private Long silverCount;

    @Column(name = "gold_count")
    private Long goldCount;

    @Column(name = "platinum_count")
    private Long platinumCount;

    @Column(name = "diamond_count")
    private Long diamondCount;

    @Column(name = "ruby_count")
    private Long rubyCount;

    @Builder
    public BadgeCondition(Member member){
        this.uuid=member.getUUID();
        this.attendance=1L;
        this.lastAttendance=LocalDate.now();
        this.baekjoon=0L;
        this.lastBaekjoon=LocalDate.now().minusDays(1L);
        this.bronzeCount=0L;
        this.silverCount=0L;
        this.goldCount=0L;
        this.platinumCount=0L;
        this.diamondCount=0L;
        this.rubyCount=0L;
    }

    public void addAttendance(){
        this.lastAttendance = LocalDate.now();
        this.attendance++;
    }

    public void addBaekjoon(String level){
        if(this.lastBaekjoon.equals(LocalDate.now().minusDays(1L))){
            this.baekjoon++;
        }else if(this.lastBaekjoon.isBefore(LocalDate.now())){
            this.baekjoon=1L;
        }
        this.lastBaekjoon=LocalDate.now();

        if(level.equals("bronze")) this.bronzeCount++;
        if(level.equals("silver")) this.silverCount++;
        if(level.equals("gold")) this.goldCount++;
        if(level.equals("platinum")) this.platinumCount++;
        if(level.equals("diamond")) this.diamondCount++;
        if(level.equals("ruby")) this.rubyCount++;
    }



    protected BadgeCondition(){}
}
