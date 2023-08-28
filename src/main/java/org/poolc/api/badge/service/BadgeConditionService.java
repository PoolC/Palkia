package org.poolc.api.badge.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.badge.domain.BadgeCondition;
import org.poolc.api.badge.repository.BadgeConditionRepository;
import org.poolc.api.member.domain.Member;
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
        BadgeCondition condition = myCondition(member);
        if(!condition.getLastAttendance().equals(LocalDate.now())){
            condition.addAttendance();
            AttendanceBadge(member,condition);
            badgeConditionRepository.save(condition);
        }
    }

    @Transactional
    public void todayBaekjoon(Member member, String level){
        BadgeCondition condition = myCondition(member);
        condition.addBaekjoon(level);
        BaekjoonBadge(member,condition);
        badgeConditionRepository.save(condition);
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

    private void AttendanceBadge(Member member, BadgeCondition condition){
        if(condition.getAttendance().equals(10L))badgeService.badgeGiver(member,2L);
        if(condition.getAttendance().equals(20L))badgeService.badgeGiver(member,3L);
        if(condition.getAttendance().equals(30L))badgeService.badgeGiver(member,4L);
        if(condition.getAttendance().equals(40L))badgeService.badgeGiver(member,5L);
        if(condition.getAttendance().equals(50L))badgeService.badgeGiver(member,6L);
        if(condition.getAttendance().equals(60L))badgeService.badgeGiver(member,7L);
        if(condition.getAttendance().equals(70L))badgeService.badgeGiver(member,8L);
        if(condition.getAttendance().equals(80L))badgeService.badgeGiver(member,9L);
        if(condition.getAttendance().equals(90L))badgeService.badgeGiver(member,10L);
        if(condition.getAttendance().equals(100L))badgeService.badgeGiver(member,11L);
    }

    private void BaekjoonBadge(Member member, BadgeCondition condition){
        if(condition.getBaekjoon().equals(3L))badgeService.badgeGiver(member,21L);
        if(condition.getBaekjoon().equals(7L))badgeService.badgeGiver(member,22L);
        if(condition.getBaekjoon().equals(14L))badgeService.badgeGiver(member,23L);
        if(condition.getBaekjoon().equals(21L))badgeService.badgeGiver(member,24L);
        if(condition.getBaekjoon().equals(28L))badgeService.badgeGiver(member,25L);

        if(condition.getBronzeCount().equals(10L))badgeService.badgeGiver(member,31L);
        if(condition.getSilverCount().equals(10L))badgeService.badgeGiver(member,32L);
        if(condition.getGoldCount().equals(10L))badgeService.badgeGiver(member,33L);
        if(condition.getPlatinumCount().equals(10L))badgeService.badgeGiver(member,34L);
        if(condition.getDiamondCount().equals(10L))badgeService.badgeGiver(member,35L);
        if(condition.getRubyCount().equals(10L))badgeService.badgeGiver(member,36L);
    }
}
