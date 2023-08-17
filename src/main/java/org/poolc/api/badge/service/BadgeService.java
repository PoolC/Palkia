package org.poolc.api.badge.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.badge.domain.Badge;
import org.poolc.api.badge.domain.BadgeLog;
import org.poolc.api.badge.dto.PostBadgeRequest;
import org.poolc.api.badge.dto.UpdateBadgeRequest;
import org.poolc.api.badge.repository.BadgeLogRepository;
import org.poolc.api.badge.repository.BadgeRepository;
import org.poolc.api.badge.vo.MyBadgeSearchResult;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.repository.MemberRepository;
import org.poolc.api.room.exception.BadRequestException;
import org.poolc.api.room.exception.ConflictException;
import org.poolc.api.room.exception.ForbiddenException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.DuplicateFormatFlagsException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BadgeService {
    private final BadgeRepository badgeRepository;
    private final BadgeLogRepository badgeLogRepository;
    private final MemberRepository memberRepository;

    public List<MyBadgeSearchResult> findMyBadge(Member member){
        return badgeLogRepository.findMyBadge(member.getUUID());
    }

    public List<Badge> findAllBadge(){
        return badgeRepository.findAllBadge();
    }

    public void createBadge(Member member, PostBadgeRequest postBadgeRequest){
        if(!member.isAdmin()){
            throw new ForbiddenException("임원진만 뱃지를 만들 수 있습니다.");
        }
        duplicateBadgeCheck(postBadgeRequest.getName());
        Badge badge = Badge.builder()
                .description(postBadgeRequest.getDescription())
                .imageUrl(postBadgeRequest.getImageUrl())
                .name(postBadgeRequest.getName())
                .build();
        badgeRepository.save(badge);
    }

    public void deleteBadge(Member member, Long badgeId){
        if(!member.isAdmin()){
            throw new ForbiddenException("임원진만 뱃지를 삭제할 수 있습니다.");
        }
        Badge badge = badgeRepository.findBadgeById(badgeId).orElseThrow(() -> new NoSuchElementException("해당하는 뱃지가 없습니다."));
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
    public void assignBadge(Member loginMember,String loginId, Long badgeId){
        if(!loginMember.isAdmin()){
            throw new ForbiddenException("임원진만 뱃지를 삭제할 수 있습니다.");
        }
        Badge badge = badgeRepository.findBadgeById(badgeId).orElseThrow(()-> new NoSuchElementException("해당하는 뱃지가 없습니다."));
        Member member = memberRepository.findByLoginID(loginId).orElseThrow(()-> new NoSuchElementException("해당하는 유저가 없습니다."));
        BadgeLog badgeLog = BadgeLog.builder()
                .date(LocalDate.now())
                .badge(badge)
                .member(member)
                .build();
        badgeLogRepository.save(badgeLog);
    }

    private void duplicateBadgeCheck(String name){
        badgeRepository.findBadgeByName(name).ifPresent(a->{throw new ConflictException("이미 있는 뱃지 이름입니다.");});
    }

    public void updateBadge(Member member,Long badgeId, UpdateBadgeRequest updateBadgeRequest) {
        if(!member.isAdmin()){
            throw new ForbiddenException("임원진만 뱃지를 수정할 수 있습니다.");
        }
        duplicateBadgeCheck(updateBadgeRequest.getName());
        Badge badge = badgeRepository.findBadgeById(badgeId).orElseThrow(() -> new NoSuchElementException("해당하는 뱃지가 없습니다."));
        badge.updateBadge(updateBadgeRequest.getName(), updateBadgeRequest.getDescription(), updateBadgeRequest.getImageUrl());
        badgeRepository.save(badge);
    }
}
