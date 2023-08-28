package org.poolc.api.baekjoon.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.badge.service.BadgeConditionService;
import org.poolc.api.baekjoon.domain.Baekjoon;
import org.poolc.api.baekjoon.domain.Problem;
import org.poolc.api.baekjoon.dto.BaekjoonResponse;
import org.poolc.api.baekjoon.exception.DuplicateBaekjoonException;
import org.poolc.api.baekjoon.repository.BaekjoonRepository;
import org.poolc.api.baekjoon.repository.ProblemRepository;
import org.poolc.api.member.domain.Member;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BaekjoonService {
    private final BaekjoonRepository baekjoonRepository;
    private final ProblemRepository problemRepository;
    private final BadgeConditionService badgeConditionService;

    public List<BaekjoonResponse> getMyBaekjoon(Member member){
        return baekjoonRepository.findMySolveLog(member.getUUID());
    }

    public void solveProblem(Member member, Long problemId, Long submissionId, String title, Long level, List<String> problemTags, String language){
        Problem problem = makeProblem(problemId,title,level,problemTags);
        logDuplicateCheck(problemId, member);
        Baekjoon baekjoon = Baekjoon.builder()
                .date(LocalDate.now())
                .problem(problem)
                .member(member)
                .language(language)
                .submissionId(submissionId)
                .build();
        baekjoonRepository.save(baekjoon);
        badgeConditionService.todayBaekjoon(member, level);
    }

    private void logDuplicateCheck(Long problemId, Member member){
        baekjoonRepository.findMyBaekjoonByProblem(member.getUUID(), problemId).ifPresent(a->{throw new DuplicateBaekjoonException("이미 처리된 문제입니다.");});
    }

    private boolean DuplicateProblemCheck(Long problemId){
        return problemRepository.findProblemByProblemId(problemId).isEmpty();
    }

    private Problem makeProblem(Long problemId, String title, Long level, List<String> problemTags){
        Problem problemResult = problemRepository.findProblemByProblemId(problemId).orElseGet(() -> {
            Problem problem = Problem.builder()
                    .problemId(problemId)
                    .title(title)
                    .level(level)
                    .tags(problemTags)
                    .build();
            problemRepository.save(problem);
            return problem;
        });
        return problemResult;
    }
}
