package org.poolc.api.board.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.auth.exception.UnauthorizedException;
import org.poolc.api.board.domain.Board;
import org.poolc.api.board.domain.BoardName;
import org.poolc.api.board.dto.BoardResponse;
import org.poolc.api.board.dto.BoardUpdateRequest;
import org.poolc.api.board.service.BoardService;
import org.poolc.api.board.vo.BoardUpdateValues;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.domain.MemberRole;
import org.poolc.api.member.domain.MemberRoles;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    /*@PostMapping("/new")
    public ResponseEntity<?> registerBoard(@RequestBody @Valid BoardCreateRequest request) {
        boardService.createBoard(new BoardCreateValues(request));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }*/

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BoardResponse>> viewBoardList(@AuthenticationPrincipal Member member) {
        List<Board> boards = boardService.findAll();
        List<BoardResponse> boardResponseList;
        if (member == null) {
            boardResponseList = boards.stream()
                    .filter(Board::isPublicReadPermission)
                    .map(BoardResponse::of)
                    .collect(Collectors.toList());
        } else {
            boardResponseList = boards.stream()
                    .filter(board -> board.memberHasReadPermissions(member.getRoles()))
                    .map(BoardResponse::of)
                    .collect(Collectors.toList());
        }
        boardResponseList.sort(Comparator.comparing(BoardResponse::getBoardId));
        return ResponseEntity.status(HttpStatus.OK).body(boardResponseList);
    }

    @GetMapping(value = "/{boardTitle}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BoardResponse> viewBoard(@AuthenticationPrincipal Member member, @PathVariable String boardTitle) {
        BoardName boardName = BoardName.getByDescription(boardTitle);
        Board board = boardService.findByBoardName(boardName);
        if (member == null) {
            checkReadPermissions(board, new MemberRoles(Set.of(MemberRole.PUBLIC)));
        } else {
            checkReadPermissions(board, member.getRoles());
        }
        return ResponseEntity.status(HttpStatus.OK).body(BoardResponse.of(board));
    }

    @PutMapping("/{boardTitle}")
    public ResponseEntity<BoardResponse> updateBoard(@AuthenticationPrincipal Member member,
                                                     @PathVariable String boardTitle,
                                                     @RequestBody @Valid BoardUpdateRequest request) {
        BoardName boardName = BoardName.getByDescription(boardTitle);
        Board board = boardService.findByBoardName(boardName);
        checkWritePermissions(board, member);
        boardService.updateBoard(board.getId(), new BoardUpdateValues(request));
        return ResponseEntity.status(HttpStatus.OK).body(BoardResponse.of(board));
    }

    @DeleteMapping("/{boardTitle}")
    public ResponseEntity<Void> deleteBoard(@AuthenticationPrincipal Member member, @PathVariable String boardTitle) {
        BoardName boardName = BoardName.getByDescription(boardTitle);
        Board board = boardService.findByBoardName(boardName);
        checkWritePermissions(board, member);
        boardService.deleteBoard(board.getId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private void checkReadPermissions(Board board, MemberRoles roles) {
        if (!board.memberHasReadPermissions(roles)) {
            throw new UnauthorizedException("접근할 수 없습니다");
        }
    }
    private void checkWritePermissions(Board board, Member member) {
        if (!board.memberHasWritePermissions(member)) {
            throw new UnauthorizedException("접근할 수 없습니다");
        }
    }

}
