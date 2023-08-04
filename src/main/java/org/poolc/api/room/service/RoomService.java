package org.poolc.api.room.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.auth.exception.UnauthorizedException;
import org.poolc.api.member.domain.Member;
import org.poolc.api.room.domain.Room;
import org.poolc.api.room.exception.BadRequestException;
import org.poolc.api.room.exception.ForbiddenException;
import org.poolc.api.room.repository.RoomRepository;
import org.poolc.api.room.vo.RoomReservation;
import org.poolc.api.room.vo.RoomReservationSearch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;

    public List<RoomReservationSearch> findReservationByDate(LocalDate start, LocalDate end){
        List<Room> roomReservationByDateRange = roomRepository.findRoomReservationByDateRange(start, end);
        List<RoomReservationSearch> searchList = new ArrayList<>();
        for (Room iter:roomReservationByDateRange) {
            searchList.add(new RoomReservationSearch(iter));
        }
        return searchList;
    }

    @Transactional
    public void reservation(RoomReservation roomReservation){
        RequestValidCheck(roomReservation);
        RoomValidCheck(roomReservation);
        Room room = Room.builder()
                .date(roomReservation.getDate())
                .startTime(roomReservation.getStart())
                .endTime(roomReservation.getEnd())
                .host(roomReservation.getHost())
                .purpose(roomReservation.getPurpose())
                .build();
        roomRepository.save(room);
    }

    @Transactional
    public void updateReservation(RoomReservation reservation, Long id){
        Room room = roomRepository.findRoomReservationById(id).orElseThrow(() -> new NoSuchElementException("해당하는 예약이 존재하지 않습니다."));
        //check host
        if(reservation.getHost().equals(room.getHost())){
            RequestValidCheck(reservation);
            RoomValidCheck(reservation);
            room.editRoom(reservation.getDate(),reservation.getStart(),reservation.getEnd(),reservation.getPurpose());
            roomRepository.save(room);
        }else{
            throw new ForbiddenException("내가 한 예약이 아닙니다.");
        }
    }

    public void cancelReservation(Member member, Long reservationId){
        Room room = roomRepository.findRoomReservationById(reservationId).orElseThrow(() -> new NoSuchElementException("해당하는 예약이 존재하지 않습니다."));
        if(member.isAdmin() || member.equals(room.getHost())){
            // 삭제
            roomRepository.delete(room);
        }else{
            throw new ForbiddenException("내가 한 예약이 아닙니다.");
        }

    }

    private void RequestValidCheck(RoomReservation roomReservation){
        if(roomReservation.getStart().isAfter(roomReservation.getEnd())){
            throw new BadRequestException("입력이 잘못되었습니다.");
        }
        if(roomReservation.getStart().getMinute()%30!=0 || roomReservation.getEnd().getMinute()%30!=0){
            throw new BadRequestException("30분 단위로만 예약할 수 있습니다.");
        }
    }

    private void RoomValidCheck(RoomReservation roomReservation){
//        if(roomRepository.validCheck(roomReservation.getDate(), roomReservation.getStart(), roomReservation.getEnd()).isExist()){
//            throw new RuntimeException("해당 시간에 예약이 존재합니다.");
//        }
//        roomRepository.validCheck(roomReservation.getDate(), roomReservation.getStart(), roomReservation.getEnd()).ifPresent(a->{throw new RuntimeException("해당 시간에 예약이 존재합니다.");});
        if(roomRepository.validCheck(roomReservation.getDate(), roomReservation.getStart(), roomReservation.getEnd())>0){
            throw new BadRequestException("해당 시간에 예약이 존재합니다.");
        }

    }


}
