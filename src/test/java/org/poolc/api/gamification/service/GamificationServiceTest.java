package org.poolc.api.gamification.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.poolc.api.common.exception.ConflictException;
import org.poolc.api.common.domain.YearSemester;
import org.poolc.api.gamification.domain.BallTransaction;
import org.poolc.api.gamification.domain.BallTransactionType;
import org.poolc.api.gamification.domain.CollectionDraw;
import org.poolc.api.gamification.domain.AchievementProgress;
import org.poolc.api.gamification.domain.CollectibleCatalog;
import org.poolc.api.gamification.domain.CollectibleRarity;
import org.poolc.api.gamification.dto.DrawResponse;
import org.poolc.api.gamification.repository.AchievementProgressRepository;
import org.poolc.api.gamification.repository.BallTransactionRepository;
import org.poolc.api.gamification.repository.CollectionDrawRepository;
import org.poolc.api.gamification.repository.CollectibleCatalogRepository;
import org.poolc.api.activity.repository.ActivityRepository;
import org.poolc.api.activity.repository.SessionRepository;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.dto.MyActivitySummaryResponse;
import org.poolc.api.member.repository.MemberRepository;
import org.poolc.api.member.service.MemberService;
import org.poolc.api.project.repository.ProjectRepository;
import org.poolc.api.scrap.repository.ScrapRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GamificationServiceTest {
    @Mock private BallTransactionRepository ballTransactionRepository;
    @Mock private CollectionDrawRepository collectionDrawRepository;
    @Mock private CollectibleCatalogRepository collectibleCatalogRepository;
    @Mock private MemberRepository memberRepository;
    @Mock private MemberService memberService;
    @Mock private AchievementProgressRepository achievementProgressRepository;
    @Mock private SessionRepository sessionRepository;
    @Mock private ActivityRepository activityRepository;
    @Mock private ScrapRepository scrapRepository;
    @Mock private ProjectRepository projectRepository;
    @Mock private Member member;

    private GamificationService service;

    @BeforeEach
    void setUp() {
        service = new GamificationService(
                ballTransactionRepository, collectionDrawRepository, collectibleCatalogRepository,
                memberRepository, memberService, achievementProgressRepository, sessionRepository,
                activityRepository, scrapRepository, projectRepository);
        when(member.getUUID()).thenReturn("member-uuid");
        lenient().when(member.getLoginID()).thenReturn("member-login-id");
        when(memberRepository.findByUUIDForUpdate("member-uuid")).thenReturn(Optional.of(member));
        lenient().when(sessionRepository.findAllWithActivityAndAttendanceInSemester(any(), any(), any())).thenReturn(Collections.emptyList());
        lenient().when(sessionRepository.findAll()).thenReturn(Collections.emptyList());
        lenient().when(activityRepository.findActivitiesByActivityMembers("member-login-id")).thenReturn(Collections.emptyList());
        lenient().when(activityRepository.findActivitiesByHost(member)).thenReturn(Collections.emptyList());
        lenient().when(scrapRepository.countByMemberIdAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(eq("member-login-id"), any(), any())).thenReturn(0L);
        lenient().when(projectRepository.findProjectsByProjectMembers("member-login-id")).thenReturn(Collections.emptyList());
        lenient().when(achievementProgressRepository.findAllByMemberUuidAndAchievementKey("member-uuid", "DAILY_ATTENDANCE"))
                .thenReturn(Collections.emptyList());
    }

    @Test
    void currentSemesterHoursGrantOnlyTheUnpaidWholeHours() {
        when(memberService.getMyActivitySummary(member)).thenReturn(activitySummary("3.7"));
        when(ballTransactionRepository.getAmountByMemberUuidAndTypeAndSource(
                "member-uuid", BallTransactionType.ACTIVITY_HOUR_REWARD, "ACTIVITY_HOURS", currentSemester()))
                .thenReturn(1L);
        when(ballTransactionRepository.getBalanceByMemberUuid("member-uuid")).thenReturn(3L);
        when(collectionDrawRepository.findAllByMemberUuidWithCollectible("member-uuid")).thenReturn(Collections.emptyList());
        when(collectibleCatalogRepository.countByEnabledTrue()).thenReturn(0L);

        service.getSummary(member);

        ArgumentCaptor<BallTransaction> transactionCaptor = ArgumentCaptor.forClass(BallTransaction.class);
        verify(ballTransactionRepository).save(transactionCaptor.capture());
        assertThat(transactionCaptor.getValue().getAmount()).isEqualTo(2);
        assertThat(transactionCaptor.getValue().getType()).isEqualTo(BallTransactionType.ACTIVITY_HOUR_REWARD);
    }

    @Test
    void decreasedActivityHoursDoNotReverseGrantedBalls() {
        when(memberService.getMyActivitySummary(member)).thenReturn(activitySummary("3"));
        when(ballTransactionRepository.getAmountByMemberUuidAndTypeAndSource(
                "member-uuid", BallTransactionType.ACTIVITY_HOUR_REWARD, "ACTIVITY_HOURS", currentSemester()))
                .thenReturn(4L);
        when(ballTransactionRepository.getBalanceByMemberUuid("member-uuid")).thenReturn(4L);
        when(collectionDrawRepository.findAllByMemberUuidWithCollectible("member-uuid")).thenReturn(Collections.emptyList());
        when(collectibleCatalogRepository.countByEnabledTrue()).thenReturn(0L);

        service.getSummary(member);

        verify(ballTransactionRepository, never()).save(any(BallTransaction.class));
    }

    @Test
    void drawWithNoBallsDoesNotConsumeOrCreateResult() {
        when(memberService.getMyActivitySummary(member)).thenReturn(activitySummary("0"));
        when(ballTransactionRepository.getAmountByMemberUuidAndTypeAndSource(
                eq("member-uuid"), eq(BallTransactionType.ACTIVITY_HOUR_REWARD), eq("ACTIVITY_HOURS"), eq(currentSemester())))
                .thenReturn(0L);
        when(ballTransactionRepository.getBalanceByMemberUuid("member-uuid")).thenReturn(0L);

        assertThatThrownBy(() -> service.draw(member)).isInstanceOf(ConflictException.class);

        verify(collectionDrawRepository, never()).save(any(CollectionDraw.class));
        verify(ballTransactionRepository, never()).save(any(BallTransaction.class));
    }

    @Test
    void drawWithBallsButNoUncollectedCatalogDoesNotConsumeABall() {
        when(memberService.getMyActivitySummary(member)).thenReturn(activitySummary("0"));
        when(ballTransactionRepository.getAmountByMemberUuidAndTypeAndSource(
                eq("member-uuid"), eq(BallTransactionType.ACTIVITY_HOUR_REWARD), eq("ACTIVITY_HOURS"), eq(currentSemester())))
                .thenReturn(0L);
        when(ballTransactionRepository.getBalanceByMemberUuid("member-uuid")).thenReturn(1L);
        for (CollectibleRarity rarity : CollectibleRarity.values()) {
            when(collectibleCatalogRepository.findUncollectedByMemberUuidAndRarity("member-uuid", rarity))
                    .thenReturn(Collections.emptyList());
        }

        assertThatThrownBy(() -> service.draw(member)).isInstanceOf(ConflictException.class)
                .hasMessageContaining("모든 포켓몬을 수집했습니다");

        verify(collectionDrawRepository, never()).save(any(CollectionDraw.class));
        verify(ballTransactionRepository, never()).save(any(BallTransaction.class));
    }

    @Test
    void drawConsumesOneBallAndCreatesTheCollectionRecordTogether() {
        CollectibleCatalog collectible = org.mockito.Mockito.mock(CollectibleCatalog.class);
        CollectionDraw savedDraw = org.mockito.Mockito.mock(CollectionDraw.class);
        when(memberService.getMyActivitySummary(member)).thenReturn(activitySummary("0"));
        when(ballTransactionRepository.getAmountByMemberUuidAndTypeAndSource(
                eq("member-uuid"), eq(BallTransactionType.ACTIVITY_HOUR_REWARD), eq("ACTIVITY_HOURS"), eq(currentSemester())))
                .thenReturn(0L);
        when(ballTransactionRepository.getBalanceByMemberUuid("member-uuid")).thenReturn(1L);
        when(collectibleCatalogRepository.findUncollectedByMemberUuidAndRarity("member-uuid", CollectibleRarity.COMMON))
                .thenReturn(List.of(collectible));
        for (CollectibleRarity rarity : List.of(CollectibleRarity.RARE, CollectibleRarity.EPIC, CollectibleRarity.LEGENDARY)) {
            when(collectibleCatalogRepository.findUncollectedByMemberUuidAndRarity("member-uuid", rarity))
                    .thenReturn(Collections.emptyList());
        }
        when(collectionDrawRepository.save(any(CollectionDraw.class))).thenReturn(savedDraw);
        when(savedDraw.getId()).thenReturn(1L);
        when(savedDraw.getCollectible()).thenReturn(collectible);
        when(savedDraw.getRarityAtDraw()).thenReturn(CollectibleRarity.COMMON);
        when(savedDraw.getDrawnAt()).thenReturn(LocalDateTime.now());
        when(collectible.getRarity()).thenReturn(CollectibleRarity.COMMON);

        DrawResponse response = service.draw(member);

        assertThat(response.getDrawId()).isEqualTo(1L);
        verify(collectionDrawRepository).save(any(CollectionDraw.class));
        ArgumentCaptor<BallTransaction> transactionCaptor = ArgumentCaptor.forClass(BallTransaction.class);
        verify(ballTransactionRepository).save(transactionCaptor.capture());
        assertThat(transactionCaptor.getValue().getAmount()).isEqualTo(-1);
        assertThat(transactionCaptor.getValue().getType()).isEqualTo(BallTransactionType.DRAW);
    }

    @Test
    void repeatableQuestClaimsOneRewardPerCompletedTargetBatch() {
        AchievementProgress progress = org.mockito.Mockito.mock(AchievementProgress.class);
        CollectionDraw draw = org.mockito.Mockito.mock(CollectionDraw.class);
        CollectibleCatalog collectible = org.mockito.Mockito.mock(CollectibleCatalog.class);
        when(draw.getDrawnAt()).thenReturn(LocalDateTime.now());
        when(draw.getCollectible()).thenReturn(collectible);
        when(collectible.getId()).thenReturn(1L);
        when(collectionDrawRepository.findAllByMemberUuidWithCollectible("member-uuid"))
                .thenReturn(Collections.nCopies(6, draw));
        when(memberService.getMyActivitySummary(member)).thenReturn(activitySummary("0"));
        when(achievementProgressRepository.findByMemberUuidAndAchievementKeyAndPeriodKey(
                eq("member-uuid"), eq("REPEAT_DRAW"), any()))
                .thenReturn(Optional.of(progress));
        when(progress.getProgress()).thenReturn(6);
        when(progress.getClaimedCount()).thenReturn(0);
        when(progress.isClaimed()).thenReturn(false);
        when(ballTransactionRepository.getBalanceByMemberUuid("member-uuid")).thenReturn(2L);

        service.claimAchievement(member, "REPEAT_DRAW");

        verify(progress).claim(2);
        ArgumentCaptor<BallTransaction> transactionCaptor = ArgumentCaptor.forClass(BallTransaction.class);
        verify(ballTransactionRepository).save(transactionCaptor.capture());
        assertThat(transactionCaptor.getValue().getAmount()).isEqualTo(2);
    }

    @Test
    void repeatableQuestRejectsAnIncompleteTargetBatch() {
        AchievementProgress progress = org.mockito.Mockito.mock(AchievementProgress.class);
        CollectionDraw draw = org.mockito.Mockito.mock(CollectionDraw.class);
        CollectibleCatalog collectible = org.mockito.Mockito.mock(CollectibleCatalog.class);
        when(draw.getDrawnAt()).thenReturn(LocalDateTime.now());
        when(draw.getCollectible()).thenReturn(collectible);
        when(collectible.getId()).thenReturn(1L);
        when(collectionDrawRepository.findAllByMemberUuidWithCollectible("member-uuid"))
                .thenReturn(List.of(draw, draw));
        when(memberService.getMyActivitySummary(member)).thenReturn(activitySummary("0"));
        when(achievementProgressRepository.findByMemberUuidAndAchievementKeyAndPeriodKey(
                eq("member-uuid"), eq("REPEAT_DRAW"), any()))
                .thenReturn(Optional.of(progress));
        when(progress.getProgress()).thenReturn(2);
        when(progress.getClaimedCount()).thenReturn(0);

        assertThatThrownBy(() -> service.claimAchievement(member, "REPEAT_DRAW"))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("받을 수 있는 보상이 없습니다");

        verify(progress, never()).claim(org.mockito.ArgumentMatchers.anyInt());
        verify(ballTransactionRepository, never()).save(any(BallTransaction.class));
    }

    private MyActivitySummaryResponse activitySummary(String totalHours) {
        return MyActivitySummaryResponse.builder()
                .totalHours(new BigDecimal(totalHours))
                .seminarStudyHours(BigDecimal.ZERO)
                .officialActivityHours(BigDecimal.ZERO)
                .projectHours(BigDecimal.ZERO)
                .seminarStudyActivities(Collections.emptyList())
                .officialActivities(Collections.emptyList())
                .projectActivities(Collections.emptyList())
                .build();
    }

    private String currentSemester() {
        return YearSemester.of(LocalDate.now()).toString();
    }
}
