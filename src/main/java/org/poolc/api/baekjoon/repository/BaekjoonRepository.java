package org.poolc.api.baekjoon.repository;

import org.poolc.api.baekjoon.domain.Baekjoon;
import org.poolc.api.baekjoon.dto.BaekjoonResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BaekjoonRepository extends JpaRepository<Baekjoon, Long> {
    @Query("select new org.poolc.api.baekjoon.dto.BaekjoonResponse(b.date, p.problemId, p.level) from Baekjoon b left join Problem p  on b.problem.id = p.id where b.member.UUID = (:uuid)")
    List<BaekjoonResponse> findMySolveLog(@Param("uuid") String uuid);

    @Query("select b from Baekjoon b where b.member.UUID = (:uuid) and b.problem.problemId = (:problemId)")
    Optional<Baekjoon> findMyBaekjoonByProblem(@Param("uuid") String uuid, @Param("problemId") Long problemId);
}
