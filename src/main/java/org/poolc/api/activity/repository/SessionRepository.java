package org.poolc.api.activity.repository;

import org.poolc.api.activity.domain.Activity;
import org.poolc.api.activity.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findByActivity(Activity activity);

    @Query(value = "select distinct s from Session s left join fetch s.attendedMemberLoginIDs where s.id=:id")
    Optional<Session> findByIdWithAttendances(@Param("id") Long id);

    @Query("select distinct s from Session s join fetch s.activity a join fetch a.host left join fetch s.attendedMemberLoginIDs where a.startDate between :semesterStartDate and :semesterEndDate and s.date <= :asOfDate")
    List<Session> findAllWithActivityAndAttendanceInSemester(
            @Param("semesterStartDate") LocalDate semesterStartDate,
            @Param("semesterEndDate") LocalDate semesterEndDate,
            @Param("asOfDate") LocalDate asOfDate
    );


}
