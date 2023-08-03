package org.poolc.api.room.vo;

import lombok.Builder;
import lombok.Getter;
import org.poolc.api.member.domain.Member;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
public class RoomReservation {
    private final LocalDate date;
    private final LocalTime start;
    private final LocalTime end;
    private final Member host;
    private String purpose;

    public RoomReservation(LocalDate date, LocalTime start, LocalTime end, Member host, String purpose) {
        this.date = date;
        this.start = start;
        this.end = end;
        this.host = host;
        this.purpose = purpose;
    }
}
