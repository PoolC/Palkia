package org.poolc.api.room.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;
import org.poolc.api.room.domain.Room;
import org.poolc.api.room.vo.RoomReservation;
import org.poolc.api.room.vo.RoomReservationSearch;

import java.util.List;

@Builder
@Getter
public class RoomGetResponse {
    private final List<RoomReservationSearch> data;

    @JsonCreator
    public RoomGetResponse(List<RoomReservationSearch> data) {
        this.data = data;
    }
}
