package org.poolc.api.attendance.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.attendance.domain.BadgeCondition;
import org.poolc.api.attendance.repository.BadgeConditionRepository;
import org.poolc.api.badge.repository.BadgeLogRepository;
import org.poolc.api.badge.service.BadgeService;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BadgeConditionService {

    private final BadgeConditionRepository badgeConditionRepository;
    private final BadgeService badgeService;

    @Transactional
    public void todayAttendance(Member member){
        BadgeCondition attendance = myCondition(member);
        if(!attendance.getLastAttendance().equals(LocalDate.now())){
            attendance.addAttendance();
            AttendanceBadge(member,attendance);
            badgeConditionRepository.save(attendance);
        }
    }

    public BadgeCondition myCondition(Member member){
        return badgeConditionRepository.myAttendance(member.getUUID()).orElseGet(()->newCondition(member));
    }

    public BadgeCondition newCondition(Member member){
        BadgeCondition attendance = BadgeCondition.builder()
                .member(member)
                .build();
        badgeConditionRepository.save(attendance);
        badgeService.badgeGiver(member,1L);
        return attendance;
    }

    public void AttendanceBadge(Member member, BadgeCondition attendance){
        if(attendance.getAttendance().equals(10L))badgeService.badgeGiver(member,2L);
        if(attendance.getAttendance().equals(20L))badgeService.badgeGiver(member,3L);
        if(attendance.getAttendance().equals(30L))badgeService.badgeGiver(member,4L);
        if(attendance.getAttendance().equals(40L))badgeService.badgeGiver(member,5L);
        if(attendance.getAttendance().equals(50L))badgeService.badgeGiver(member,6L);
        if(attendance.getAttendance().equals(60L))badgeService.badgeGiver(member,7L);
        if(attendance.getAttendance().equals(70L))badgeService.badgeGiver(member,8L);
        if(attendance.getAttendance().equals(80L))badgeService.badgeGiver(member,9L);
        if(attendance.getAttendance().equals(90L))badgeService.badgeGiver(member,10L);
        if(attendance.getAttendance().equals(100L))badgeService.badgeGiver(member,11L);
    }
}
