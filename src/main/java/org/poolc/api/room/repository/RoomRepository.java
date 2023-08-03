package org.poolc.api.room.repository;

import org.poolc.api.room.domain.Room;
import org.poolc.api.room.vo.RoomReservationValidResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("select r from Room r where r.date=(:date)")
    List<Room> findRoomReservationByDate(@Param("date") LocalDate date);

    @Query("select r from Room r where r.date between (:start) and (:end)")
    List<Room> findRoomReservationByDateRange(@Param("start") LocalDate startDate, @Param("end") LocalDate endDate);

    @Query("select r from Room r where r.id=(:id)")
    Optional<Room> findRoomReservationById(@Param("id") Long id);

    @Query("select count(r) from Room r where r.date=(:date) and not((r.startTime<(:startTime) and r.endTime<=(:startTime)) or (r.startTime>=(:endTime) and r.endTime>(:endTime))) ")
    Long validCheck(@Param("date") LocalDate date, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);

}
