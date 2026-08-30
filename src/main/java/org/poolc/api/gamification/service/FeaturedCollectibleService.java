package org.poolc.api.gamification.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.common.exception.ConflictException;
import org.poolc.api.gamification.domain.CollectibleCatalog;
import org.poolc.api.gamification.domain.MemberFeaturedCollectible;
import org.poolc.api.gamification.dto.FeaturedCollectibleResponse;
import org.poolc.api.gamification.dto.UpdateFeaturedCollectibleRequest;
import org.poolc.api.gamification.repository.CollectibleCatalogRepository;
import org.poolc.api.gamification.repository.CollectionDrawRepository;
import org.poolc.api.gamification.repository.MemberFeaturedCollectibleRepository;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeaturedCollectibleService {
    private final MemberFeaturedCollectibleRepository featuredCollectibleRepository;
    private final CollectibleCatalogRepository collectibleCatalogRepository;
    private final CollectionDrawRepository collectionDrawRepository;
    private final MemberRepository memberRepository;

    public Optional<FeaturedCollectibleResponse> getFeatured(Member member) {
        return featuredCollectibleRepository.findByMemberUuid(member.getUUID())
                .map(FeaturedCollectibleResponse::new);
    }

    public Optional<String> getProfileSpriteUrl(Member member) {
        return featuredCollectibleRepository.findByMemberUuid(member.getUUID())
                .filter(MemberFeaturedCollectible::isUseAsProfile)
                .map(featured -> featured.isShiny()
                        ? featured.getCollectible().getShinySpriteUrl()
                        : featured.getCollectible().getSpriteUrl());
    }

    public Map<String, String> getProfileSpriteUrls(Collection<Member> members) {
        Map<String, String> originalProfileImageUrls = new HashMap<>();
        for (Member member : members) {
            originalProfileImageUrls.put(member.getUUID(), member.getProfileImageURL());
        }
        if (originalProfileImageUrls.isEmpty()) {
            return originalProfileImageUrls;
        }
        featuredCollectibleRepository.findAllByMemberUuidIn(originalProfileImageUrls.keySet()).stream()
                .filter(MemberFeaturedCollectible::isUseAsProfile)
                .forEach(featured -> originalProfileImageUrls.put(featured.getMember().getUUID(), featured.isShiny()
                        ? featured.getCollectible().getShinySpriteUrl()
                        : featured.getCollectible().getSpriteUrl()));
        return originalProfileImageUrls;
    }

    @Transactional
    public FeaturedCollectibleResponse updateFeatured(Member authenticatedMember, UpdateFeaturedCollectibleRequest request) {
        if (request.getCollectibleId() == null) {
            throw new ConflictException("대표 포켓몬을 선택해주세요.");
        }
        Member member = findMemberForUpdate(authenticatedMember);
        CollectibleCatalog collectible = collectibleCatalogRepository.findById(request.getCollectibleId())
                .orElseThrow(() -> new NoSuchElementException("포켓몬을 찾을 수 없습니다."));
        if (!collectionDrawRepository.existsByMemberUuidAndCollectibleIdAndShiny(member.getUUID(), collectible.getId(), request.isShiny())) {
            throw new ConflictException("보유하지 않은 포켓몬은 대표로 지정할 수 없습니다.");
        }
        MemberFeaturedCollectible featured = featuredCollectibleRepository.findByMemberUuid(member.getUUID())
                .orElseGet(() -> new MemberFeaturedCollectible(member, collectible, request.isShiny()));
        featured.updateCollectible(collectible, request.isShiny());
        return new FeaturedCollectibleResponse(featuredCollectibleRepository.save(featured));
    }

    @Transactional
    public FeaturedCollectibleResponse updateUseAsProfile(Member authenticatedMember, boolean useAsProfile) {
        Member member = findMemberForUpdate(authenticatedMember);
        MemberFeaturedCollectible featured = featuredCollectibleRepository.findByMemberUuid(member.getUUID())
                .orElseThrow(() -> new ConflictException("대표 포켓몬을 먼저 지정해주세요."));
        featured.updateUseAsProfile(useAsProfile);
        return new FeaturedCollectibleResponse(featured);
    }

    @Transactional
    public void clearFeatured(Member authenticatedMember) {
        Member member = findMemberForUpdate(authenticatedMember);
        featuredCollectibleRepository.findByMemberUuid(member.getUUID())
                .ifPresent(featuredCollectibleRepository::delete);
    }

    private Member findMemberForUpdate(Member authenticatedMember) {
        return memberRepository.findByUUIDForUpdate(authenticatedMember.getUUID())
                .orElseThrow(() -> new NoSuchElementException("회원을 찾을 수 없습니다."));
    }
}
