package org.poolc.api.room.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class RoomUpdateRequest {

    private final LocalDate date;
    private final LocalTime start;
    private final LocalTime end;
    private final String purpose;

    public RoomUpdateRequest(LocalDate date, LocalTime start, LocalTime end, String purpose) {
        this.date = date;
        this.start = start;
        this.end = end;
        this.purpose = purpose;
    }
}
