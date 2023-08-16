package org.poolc.api.room.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class RoomReservationRequest {

    private final LocalDate date;
    private final LocalTime start;
    private final LocalTime end;
    private final String purpose;

    @JsonCreator
    public RoomReservationRequest(LocalDate date, LocalTime start, LocalTime end, String purpose) {
        this.date = date;
        this.start = start;
        this.end = end;
        this.purpose = purpose;
    }
}
