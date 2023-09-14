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

    @Column(name = "like_count")
    private Long likeCount;

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
        this.likeCount=0L;
    }

    public void addAttendance(){
        this.lastAttendance = LocalDate.now();
        this.attendance++;
    }

    public void addBaekjoon(Long level){
        if(this.lastBaekjoon.equals(LocalDate.now().minusDays(1L))){
            this.baekjoon++;
        }else if(this.lastBaekjoon.isBefore(LocalDate.now())){
            this.baekjoon=1L;
        }
        this.lastBaekjoon=LocalDate.now();

        if(level <=5) this.bronzeCount++;
        if(6<=level && level <=10) this.silverCount++;
        if(11<=level && level <=15) this.goldCount++;
        if(16<=level && level <=20) this.platinumCount++;
        if(21<=level && level <=25) this.diamondCount++;
        if(26<=level) this.rubyCount++;
    }

    public void addLike(){
        this.likeCount++;
    }

    public void dislike(){
        this.likeCount--;
    }

    protected BadgeCondition(){}
}
