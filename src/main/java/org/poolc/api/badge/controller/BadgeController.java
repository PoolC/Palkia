package org.poolc.api.badge.controller;

import io.swagger.models.Response;
import lombok.RequiredArgsConstructor;
import org.poolc.api.badge.domain.BadgeCondition;
import org.poolc.api.badge.dto.*;
import org.poolc.api.badge.service.BadgeConditionService;
import org.poolc.api.badge.service.BadgeService;
import org.poolc.api.badge.vo.BadgeWithOwn;
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
    private final BadgeConditionService badgeConditionService;

    @GetMapping
    public ResponseEntity<GetMyBadgeResponse> getMyBadge(@AuthenticationPrincipal Member member){
        GetMyBadgeResponse getMyBadgeResponse = GetMyBadgeResponse.builder()
                .data(badgeService.findMyBadge(member))
                .build();
        return ResponseEntity.ok().body(getMyBadgeResponse);
    }

    @GetMapping(path="/all")
    public ResponseEntity<GetAllBadgeResponse> getAllBadge(@AuthenticationPrincipal Member member){
        BadgeCondition badgeCondition = badgeConditionService.myCondition(member);
        GetAllBadgeResponse getAllBadgeResponse = GetAllBadgeResponse.builder()
                .data(badgeService.findAllBadgeWithOwnCount(member))
                .condition(badgeCondition)
                .build();
        return ResponseEntity.ok().body(getAllBadgeResponse);
    }

    @PostMapping
    public ResponseEntity<Void> postBadge(@AuthenticationPrincipal Member member, @RequestBody PostBadgeRequest postBadgeRequest){
        badgeService.adminCheck(member,"임원진만 뱃지를 만들 수 있습니다.");
        badgeService.createBadge(postBadgeRequest);
        notificationService.createBadgeNotification(member.getLoginID());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/select/{badgeId}")
    public ResponseEntity<Void> selectBadge(@AuthenticationPrincipal Member member, @PathVariable Long badgeId){
        badgeService.selectBadge(member, badgeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/assign/{loginId}")
    public ResponseEntity<GetOtherBadgeResponse> getMemberBadge(@AuthenticationPrincipal Member admin, @PathVariable String loginId){
        badgeService.adminCheck(admin,"임원진만 뱃지를 지급할 수 있습니다.");
        GetOtherBadgeResponse response = GetOtherBadgeResponse.builder()
                .data(badgeService.findAllBadgeWithOwn(loginId))
                .build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/assign/{loginId}")
    public ResponseEntity<Void> assignBadge(@AuthenticationPrincipal Member member, @PathVariable String loginId, @RequestBody AssignBadgeRequest assignBadgeRequest){
        badgeService.adminCheck(member,"임원진만 뱃지를 지급할 수 있습니다.");
        badgeService.assignBadge(loginId, assignBadgeRequest.getData());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path="/{badgeId}")
    public ResponseEntity<Void> deleteBadge(@AuthenticationPrincipal Member member, @PathVariable Long badgeId){
        badgeService.adminCheck(member,"임원진만 뱃지를 삭제할 수 있습니다.");
        badgeService.deleteBadge(badgeId);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path="/{badgeId}")
    public ResponseEntity<Void> updateBadge(@AuthenticationPrincipal Member member, @PathVariable Long badgeId, @RequestBody UpdateBadgeRequest updateBadgeRequest){
        badgeService.adminCheck(member,"임원진만 뱃지를 수정할 수 있습니다.");
        badgeService.updateBadge(badgeId, updateBadgeRequest);
        return ResponseEntity.ok().build();
    }

}
