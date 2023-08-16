package org.poolc.api.room.controller;


import lombok.RequiredArgsConstructor;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.service.MemberService;
import org.poolc.api.room.domain.Room;
import org.poolc.api.room.dto.*;
import org.poolc.api.room.service.RoomService;
import org.poolc.api.room.vo.RoomReservation;
import org.poolc.api.room.vo.RoomReservationSearch;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value="/room", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoomController {
    private final RoomService roomService;
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<RoomGetResponse> findRoomReservation(@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate start, @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate end ){
        List<RoomReservationSearch> reservations = roomService.findReservationByDate(start,end);
        RoomGetResponse roomGetResponse = RoomGetResponse.builder()
                .data(reservations)
                .build();
        return ResponseEntity.ok().body(roomGetResponse);
    }

    @PostMapping
    public ResponseEntity<Void> createRoomReservation(@AuthenticationPrincipal Member member, @RequestBody RoomPostRequest roomPostRequest){
        RoomReservation reservation = RoomReservation.builder()
                .host(member)
                .date(roomPostRequest.getDate())
                .start(roomPostRequest.getStart())
                .end(roomPostRequest.getEnd())
                .purpose(roomPostRequest.getPurpose())
                .build();
        roomService.reservation(reservation);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path="/{reservationId}")
    public ResponseEntity<Void> putRoomReservation(@AuthenticationPrincipal Member member,@PathVariable Long reservationId,@RequestBody RoomUpdateRequest roomUpdateRequest){
        roomService.updateReservation(RoomReservation.builder()
                        .host(member)
                        .date(roomUpdateRequest.getDate())
                        .start(roomUpdateRequest.getStart())
                        .end(roomUpdateRequest.getEnd())
                        .purpose(roomUpdateRequest.getPurpose())
                .build(),
                reservationId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path="/{reservationId}")
    public ResponseEntity<Void> deleteRoomReservation(@AuthenticationPrincipal Member member,@PathVariable Long reservationId){
        roomService.cancelReservation(member,reservationId);
        return ResponseEntity.ok().build();
    }
}
