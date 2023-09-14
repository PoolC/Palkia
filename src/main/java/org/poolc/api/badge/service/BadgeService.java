package org.poolc.api.badge.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.badge.domain.Badge;
import org.poolc.api.badge.domain.BadgeLog;
import org.poolc.api.badge.dto.PostBadgeRequest;
import org.poolc.api.badge.dto.UpdateBadgeRequest;
import org.poolc.api.badge.repository.BadgeLogRepository;
import org.poolc.api.badge.repository.BadgeRepository;
import org.poolc.api.badge.vo.*;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.repository.MemberRepository;
import org.poolc.api.notification.service.NotificationService;
import org.poolc.api.room.exception.ConflictException;
import org.poolc.api.room.exception.ForbiddenException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BadgeService {
    private final BadgeRepository badgeRepository;
    private final BadgeLogRepository badgeLogRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    //내가 받은 뱃지 조회
    public List<MyBadgeSearchResult> findMyBadge(Member member){
        return badgeLogRepository.findMyBadge(member.getUUID());
    }

    //모든 뱃지 조회
    public List<Badge> findAllBadge(){
        return badgeRepository.findAllBadge();
    }

    //모든 뱃지 중에 내가 얻은 뱃지
    public List<BadgeWithOwnCount> findAllBadgeWithOwnCount(Member member){
        List<BadgeWithCount> allBadgeWithCount = badgeRepository.findAllBadgeWithCount();
        List<BadgeWithOwnCount> data = badgeRepository.findAllBadgeWithOwn(member.getUUID()).stream().map(h -> new BadgeWithOwnCount(h)).collect(Collectors.toList());
        for (BadgeWithOwnCount badge:data) {
            allBadgeWithCount.stream().filter(c->c.getId().equals(badge.getId())).findFirst().ifPresent(c -> badge.setCount(c.getCount()));
        }
        return data;
    }

    public List<BadgeWithOwn> findAllBadgeWithOwn(String loginId){
        Member member = memberRepository.findByLoginID(loginId).orElseThrow(()-> new NoSuchElementException("해당하는 유저가 없습니다."));
        return badgeRepository.findAllBadgeWithOwn(member.getUUID());
    }

    public void createBadge(PostBadgeRequest postBadgeRequest){
        duplicateBadgeCheck(postBadgeRequest.getName());
        Badge badge = Badge.builder()
                .description(postBadgeRequest.getDescription())
                .imageUrl(postBadgeRequest.getImageUrl())
                .name(postBadgeRequest.getName())
                .build();
        badgeRepository.save(badge);
    }

    public void deleteBadge(Long badgeId){
        Badge badge = badgeRepository.findBadgeById(badgeId).orElseThrow(() -> new NoSuchElementException("해당하는 뱃지가 없습니다."));
        if(!badge.getCategory().equals(BadgeCategory.ETC)) throw new ForbiddenException("해당 배지는 삭제할 수 없습니다.");
        List<Member> badgeUser = memberRepository.findBadgeUser(badge.getId());
        for (Member m:badgeUser) {
            m.deleteBadge();
            memberRepository.save(m);
        }
        badgeRepository.delete(badge);
    }

    public void selectBadge(Member member, Long badgeId){
        Badge badge = badgeRepository.findBadgeById(badgeId).orElseThrow(() -> new NoSuchElementException("해당하는 뱃지가 없습니다."));
        Long result = badgeLogRepository.myBadgeCheck(member.getUUID(), badgeId);
        if(result==0){
            throw new ForbiddenException("내가 얻은 뱃지가 아닙니다.");
        }
        member.updateBadge(badge);
        memberRepository.save(member);
    }

    @Transactional
    public void assignBadge(String loginId, List<AssignBadge> request){
        Member member = memberRepository.findByLoginID(loginId).orElseThrow(()-> new NoSuchElementException("해당하는 유저가 없습니다."));
        List<MyBadgeSearchResult> myBadge = findMyBadge(member);

        for (AssignBadge ab:request) {
            Badge badge = badgeRepository.findBadgeById(ab.getId()).get();
            if(ab.getOwn() && !myBadge.stream().filter(b->b.getId().equals(ab.getId())).findFirst().isPresent()){
                BadgeLog badgeLog = BadgeLog.builder()
                        .badge(badge)
                        .date(LocalDate.now())
                        .member(member)
                        .build();
                badgeLogRepository.save(badgeLog);
            }else if(!ab.getOwn() && myBadge.stream().filter(b->b.getId().equals(ab.getId())).findFirst().isPresent()){
                BadgeLog badgeLog = badgeLogRepository.findBadgeLogByUUID(member.getUUID(), badge.getId()).get();
                badgeLogRepository.delete(badgeLog);
                if(member.getBadge().getId().equals(ab.getId())){
                    member.deleteBadge();
                    memberRepository.save(member);
                }
            }
        }
    }

    private void duplicateBadgeCheck(String name){
        badgeRepository.findBadgeByName(name).ifPresent(a->{throw new ConflictException("이미 있는 뱃지 이름입니다.");});
    }

    private boolean duplicateBadgeLogCheck(Long badgeId, Member member){
        return !badgeLogRepository.findBadgeLogByUUID(member.getUUID(), badgeId).isPresent();
    }

    public void updateBadge(Long badgeId, UpdateBadgeRequest updateBadgeRequest) {
        Badge badge = badgeRepository.findBadgeById(badgeId).orElseThrow(() -> new NoSuchElementException("해당하는 뱃지가 없습니다."));
        if(!badge.getName().equals(updateBadgeRequest.getName())) duplicateBadgeCheck(updateBadgeRequest.getName());
        badge.updateBadge(updateBadgeRequest.getName(), updateBadgeRequest.getDescription(), updateBadgeRequest.getImageUrl());
        badgeRepository.save(badge);
    }

    public void adminCheck(Member member, String message){
        if(!member.isAdmin()){
            throw new ForbiddenException(message);
        }
    }

    public Badge getBadgeByBadgeId(Long badgeId){
        return badgeRepository.findBadgeById(badgeId).get();
    }

    //뱃지가 존재하는 경우에만 지급함.
    public void badgeGiver(Member member, Long badgeId){
        if(duplicateBadgeLogCheck(badgeId, member)&&badgeRepository.findBadgeById(badgeId).isPresent()){
            Badge badge = getBadgeByBadgeId(badgeId);
            badgeLogRepository.save(BadgeLog.builder()
                    .member(member)
                    .date(LocalDate.now())
                    .badge(badge)
                    .build());
        }
    }
}
