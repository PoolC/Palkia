package org.poolc.api.gamification.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.auth.exception.UnauthorizedException;
import org.poolc.api.gamification.domain.CatalogSyncRun;
import org.poolc.api.gamification.dto.CatalogSyncRunResponse;
import org.poolc.api.gamification.dto.BallBalancesResponse;
import org.poolc.api.gamification.dto.AchievementResponse;
import org.poolc.api.gamification.dto.CollectionItemResponse;
import org.poolc.api.gamification.dto.DrawResponse;
import org.poolc.api.gamification.dto.DrawCollectibleRequest;
import org.poolc.api.gamification.dto.GameSummaryResponse;
import org.poolc.api.gamification.dto.FeaturedCollectibleResponse;
import org.poolc.api.gamification.dto.UpdateFeaturedCollectibleRequest;
import org.poolc.api.gamification.dto.UpdateFeaturedProfileRequest;
import org.poolc.api.gamification.service.CatalogSyncService;
import org.poolc.api.gamification.service.CatalogSyncWorker;
import org.poolc.api.gamification.service.FeaturedCollectibleService;
import org.poolc.api.gamification.service.GamificationService;
import org.poolc.api.member.domain.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gamification")
public class GamificationController {
    private final GamificationService gamificationService;
    private final CatalogSyncService catalogSyncService;
    private final CatalogSyncWorker catalogSyncWorker;
    private final FeaturedCollectibleService featuredCollectibleService;

    @GetMapping("/me/summary")
    public ResponseEntity<GameSummaryResponse> getSummary(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(gamificationService.getSummary(member));
    }

    @GetMapping("/me/collection")
    public ResponseEntity<List<CollectionItemResponse>> getCollection(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(gamificationService.getCollection(member));
    }

    @GetMapping("/me/draws")
    public ResponseEntity<List<DrawResponse>> getDraws(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(gamificationService.getDraws(member));
    }

    @GetMapping("/me/featured")
    public ResponseEntity<FeaturedCollectibleResponse> getFeatured(@AuthenticationPrincipal Member member) {
        return featuredCollectibleService.getFeatured(member)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PutMapping("/me/featured")
    public ResponseEntity<FeaturedCollectibleResponse> updateFeatured(
            @AuthenticationPrincipal Member member, @RequestBody UpdateFeaturedCollectibleRequest request) {
        return ResponseEntity.ok(featuredCollectibleService.updateFeatured(member, request));
    }

    @PutMapping("/me/featured/profile")
    public ResponseEntity<FeaturedCollectibleResponse> updateFeaturedProfile(
            @AuthenticationPrincipal Member member, @RequestBody UpdateFeaturedProfileRequest request) {
        return ResponseEntity.ok(featuredCollectibleService.updateUseAsProfile(member, request.isUseAsProfile()));
    }

    @DeleteMapping("/me/featured")
    public ResponseEntity<Void> clearFeatured(@AuthenticationPrincipal Member member) {
        featuredCollectibleService.clearFeatured(member);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/me/draws")
    public ResponseEntity<DrawResponse> draw(
            @AuthenticationPrincipal Member member,
            @RequestBody DrawCollectibleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gamificationService.draw(member, request.isShiny()));
    }

    @GetMapping("/me/achievements")
    public ResponseEntity<List<AchievementResponse>> getAchievements(@AuthenticationPrincipal Member member, HttpServletRequest request) {
        return ResponseEntity.ok(gamificationService.getAchievements(member, request));
    }

    @PostMapping("/me/achievements/{achievementKey}/claim")
    public ResponseEntity<BallBalancesResponse> claimAchievement(
            @AuthenticationPrincipal Member member, @PathVariable String achievementKey, HttpServletRequest request) {
        return ResponseEntity.ok(gamificationService.claimAchievement(member, achievementKey, request));
    }

    @PostMapping("/admin/catalog/sync")
    public ResponseEntity<CatalogSyncRunResponse> startCatalogSync(@AuthenticationPrincipal Member member) {
        requireAdmin(member);
        CatalogSyncRun run = catalogSyncService.startSync();
        catalogSyncWorker.synchronize(run.getId());
        return ResponseEntity.accepted().body(new CatalogSyncRunResponse(run));
    }

    @GetMapping("/admin/catalog/sync/latest")
    public ResponseEntity<CatalogSyncRunResponse> getLatestCatalogSync(@AuthenticationPrincipal Member member) {
        requireAdmin(member);
        return catalogSyncService.getLatestRun()
                .map(run -> ResponseEntity.ok(new CatalogSyncRunResponse(run)))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    private void requireAdmin(Member member) {
        if (member == null || !member.isAdmin()) {
            throw new UnauthorizedException("관리자 권한이 필요합니다.");
        }
    }
}
