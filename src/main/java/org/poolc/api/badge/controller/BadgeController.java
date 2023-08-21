package org.poolc.api.badge.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.badge.dto.*;
import org.poolc.api.badge.service.BadgeService;
import org.poolc.api.badge.vo.MyBadgeSearchResult;
import org.poolc.api.member.domain.Member;
import org.poolc.api.notification.domain.NotificationType;
import org.poolc.api.notification.service.NotificationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value="/badge", produces = MediaType.APPLICATION_JSON_VALUE)
public class BadgeController {
    private final BadgeService badgeService;
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<GetMyBadgeResponse> getMyBadge(@AuthenticationPrincipal Member member){
        GetMyBadgeResponse getMyBadgeResponse = GetMyBadgeResponse.builder()
                .data(badgeService.findMyBadge(member))
                .build();
        return ResponseEntity.ok().body(getMyBadgeResponse);
    }

    @GetMapping(path="/all")
    public ResponseEntity<GetAllBadgeResponse> getAllBadge(){
        GetAllBadgeResponse getAllBadgeResponse = GetAllBadgeResponse.builder()
                .data(badgeService.findAllBadge())
                .build();
        return ResponseEntity.ok().body(getAllBadgeResponse);
    }

    @PostMapping
    public ResponseEntity<Void> postBadge(@AuthenticationPrincipal Member member, @RequestBody PostBadgeRequest postBadgeRequest){
        badgeService.createBadge(member, postBadgeRequest);
        notificationService.createNotification(null, member, NotificationType.BADGE);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/select/{badgeId}")
    public ResponseEntity<Void> selectBadge(@AuthenticationPrincipal Member member, @PathVariable Long badgeId){
        badgeService.selectBadge(member, badgeId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/assign")
    public ResponseEntity<Void> assignBadge(@AuthenticationPrincipal Member member, @RequestBody AssignBadgeRequest assignBadgeRequest){
        badgeService.assignBadge(member, assignBadgeRequest.getLoginId(), assignBadgeRequest.getBadgeId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path="/{badgeId}")
    public ResponseEntity<Void> deleteBadge(@AuthenticationPrincipal Member member, @PathVariable Long badgeId){
        badgeService.deleteBadge(member,badgeId);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path="/{badgeId}")
    public ResponseEntity<Void> updateBadge(@AuthenticationPrincipal Member member, @PathVariable Long badgeId, @RequestBody UpdateBadgeRequest updateBadgeRequest){
        badgeService.updateBadge(member, badgeId, updateBadgeRequest);
        return ResponseEntity.ok().build();
    }

}
