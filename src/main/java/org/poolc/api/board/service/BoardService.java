package org.poolc.api.board.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.board.domain.Board;
import org.poolc.api.board.repository.BoardRepository;
import org.poolc.api.board.vo.BoardCreateValues;
import org.poolc.api.board.vo.BoardUpdateValues;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    public Board findById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new NoSuchElementException("No board found with given board id."));
    }

    public Board findByUrlPath(String urlPath) {
        return boardRepository.findByUrlPath(urlPath)
                .orElseThrow(() -> new NoSuchElementException("No board found with given board id."));
    }

    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    public void createBoard(BoardCreateValues values) {
        if (!boardRepository.existsByNameOrUrlPath(values.getName(), values.getUrlPath())) {
            boardRepository.save(new Board(values));
        }
    }

    public void updateBoard(Long boardId, BoardUpdateValues values) {
        Board board = findById(boardId);
        board.updateBoard(values);
    }

    public void deleteBoard(Long boardId) {
        Board board = findById(boardId);
        board.setIsDeleted();
    }

    public Board findByName(String name) {
        return boardRepository.findByName(name)
                .orElseThrow(() -> new NoSuchElementException("No board with given name."));

    }
}
