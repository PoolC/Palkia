package org.poolc.api.member.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.gamification.service.FeaturedCollectibleService;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.dto.MemberResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MemberResponseAssembler {
    private final FeaturedCollectibleService featuredCollectibleService;

    public MemberResponse of(Member member) {
        String profileImageUrl = featuredCollectibleService.getProfileSpriteUrl(member)
                .orElse(member.getProfileImageURL());
        return MemberResponse.of(member, profileImageUrl);
    }

    public List<MemberResponse> ofAll(List<Member> members) {
        Map<String, String> profileImageUrls = featuredCollectibleService.getProfileSpriteUrls(members);
        return members.stream()
                .map(member -> MemberResponse.of(member, profileImageUrls.get(member.getUUID())))
                .collect(Collectors.toList());
    }
}
