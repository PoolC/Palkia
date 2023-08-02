package org.poolc.api.board.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.board.dto.BoardCreateRequest;
import org.poolc.api.board.dto.BoardResponse;
import org.poolc.api.board.service.BoardService;
import org.poolc.api.board.vo.BoardCreateValues;
import org.poolc.api.member.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/new")
    public ResponseEntity<?> registerBoard(@RequestBody @Valid BoardCreateRequest request) {
        boardService.createBoard(new BoardCreateValues(request));
        return ResponseEntity.ok().build();
    }
}
