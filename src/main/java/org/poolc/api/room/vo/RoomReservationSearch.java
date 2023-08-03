package org.poolc.api.room.vo;

import lombok.Builder;
import lombok.Getter;
import org.poolc.api.member.domain.Member;
import org.poolc.api.room.domain.Room;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
public class RoomReservationSearch {
    private final Long id;
    private final LocalDate date;
    private final LocalTime start;
    private final LocalTime end;
    private final String host;
    private String purpose;

    public RoomReservationSearch(Long id,LocalDate date, LocalTime start, LocalTime end, String host, String purpose) {
        this.id=id;
        this.date = date;
        this.start = start;
        this.end = end;
        this.host = host;
        this.purpose = purpose;
    }

    public RoomReservationSearch(Room room){
        this.id = room.getId();
        this.date = room.getDate();
        this.end=room.getEndTime();
        this.start=room.getStartTime();
        this.purpose=room.getPurpose();
        this.host=room.getHost().getName();
    }
}
