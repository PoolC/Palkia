package org.poolc.api.baekjoon.repository;

import org.poolc.api.baekjoon.domain.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {

    @Query("select p from Problem p where p.problemId=(:problemId)")
    Optional<Problem> findProblemByProblemId(@Param("problemId") Long problemId);
}
