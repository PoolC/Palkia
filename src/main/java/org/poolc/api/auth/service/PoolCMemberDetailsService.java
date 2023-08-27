package org.poolc.api.auth.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.badge.service.BadgeConditionService;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PoolCMemberDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final BadgeConditionService badgeConditionService;

    @Override
    public UserDetails loadUserByUsername(String memberUUID) throws UsernameNotFoundException {
        Member member = memberRepository.findByUUID(memberUUID)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("Member %s does not exist", memberUUID)));
        badgeConditionService.todayAttendance(member);
        return member;
    }
}
