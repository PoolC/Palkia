package org.poolc.api.room.domain;

import lombok.Builder;
import lombok.Getter;
import org.poolc.api.member.domain.Member;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Table(name = "Room")
public class Room {

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "purpose", nullable = true)
    private String purpose;

    @ManyToOne
    @JoinColumn(name = "host", nullable=false, referencedColumnName = "uuid")
    private Member host;

    @Builder
    public Room(Long id, LocalDate date, LocalTime startTime, LocalTime endTime, String purpose, Member host) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.purpose = purpose;
        this.host = host;
    }

    protected Room() {

    }

    public void editRoom(LocalDate date, LocalTime startTime, LocalTime endTime, String purpose){
        this.date=date;
        this.startTime=startTime;
        this.endTime=endTime;
        this.purpose=purpose;
    }
}
