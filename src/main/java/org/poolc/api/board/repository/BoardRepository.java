package org.poolc.api.board.repository;

import org.poolc.api.board.domain.Board;
import org.poolc.api.board.domain.BoardName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByUrlPath(String urlPath);
    boolean existsByBoardNameOrUrlPath(BoardName boardName, String urlPath);

    Optional<Board> findByBoardName(BoardName boardName);
}
