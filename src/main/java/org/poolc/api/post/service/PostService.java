package org.poolc.api.post.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.board.domain.Board;
import org.poolc.api.board.service.BoardService;
import org.poolc.api.member.domain.Member;
import org.poolc.api.post.domain.Post;
import org.poolc.api.post.domain.PostType;
import org.poolc.api.post.dto.PostCreateRequest;
import org.poolc.api.post.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final BoardService boardService;

    @Transactional
    public Long createPost(Member member, PostCreateRequest postCreateRequest) {
        Board board = boardService.findById(postCreateRequest.getBoardId());

        //permission check

        return 0L;

    }

    private void checkWritePermission(Member member, Board board) {

    }
    private void checkReadPermission(Member member, Board board) {

    }
}
