package org.poolc.api.room.vo;

import lombok.Getter;

@Getter
public class RoomReservationValidResult {
    private final boolean exist;

    public RoomReservationValidResult(boolean exist) {
        this.exist = exist;
    }
}
