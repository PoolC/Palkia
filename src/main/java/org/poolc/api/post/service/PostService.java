package org.poolc.api.post.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.auth.exception.UnauthorizedException;
import org.poolc.api.board.domain.Board;
import org.poolc.api.board.service.BoardService;
import org.poolc.api.comment.domain.Comment;
import org.poolc.api.member.domain.Member;
import org.poolc.api.post.domain.Post;
import org.poolc.api.post.dto.PostCreateRequest;
import org.poolc.api.post.dto.PostResponse;
import org.poolc.api.post.repository.PostRepository;
import org.poolc.api.post.vo.PostCreateValues;
import org.poolc.api.post.vo.PostUpdateValues;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final BoardService boardService;

    public Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("No post found with given post id."));
    }

    @Transactional(readOnly = true)
    public List<PostResponse> findPostsByMember(Member member, int page, int size) {
        PageRequest pr = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findByMember(member, pr);
        if (posts.getNumberOfElements() == 0) return null;
        return posts.stream()
                .map(PostResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> findPostsByBoard(Board board, int page, int size) {
        PageRequest pr = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findByBoard(board, pr);
        if (posts.getNumberOfElements() == 0) return null;
        return posts.stream()
                .map(PostResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long createPost(Member member, PostCreateValues values) {
        Board board = boardService.findById(values.getBoard().getId());
        checkWritePermission(member, board);
        Post post = new Post(board, member, values);
        postRepository.save(post);
        return post.getId();
    }

    @Transactional
    public void updatePost(Member member, Long postId, PostUpdateValues values) {
        Post post = findPostById(postId);
        checkWriter(member, post);
        post.updatePost(post.getPostType(), values);
    }

    private void checkWritePermission(Member member, Board board) {
        if (!board.memberHasWritePermissions(member)) throw new UnauthorizedException("접근할 수 없습니다.");
    }
    private void checkReadPermission(Member user, Board board) {
        if ((user == null && !board.isPublicReadPermission()) || (user != null && !board.memberHasReadPermissions(user.getRoles()))) {
            throw new UnauthorizedException("접근할 수 없습니다.");
        }
    }

    private void checkWriter(Member member, Post post) {
        if (!post.getMember().equals(member)) throw new UnauthorizedException("접근할 수 없습니다.");
    }

    private void checkWriterOrAdmin(Member user, Post post) {
        if (!post.getMember().equals(user) && !user.isAdmin()) throw new UnauthorizedException("접근할 수 없습니다.");
    }

    public void likePost(Member member, Long postId) {
        Post post = findPostById(postId);
        checkNotWriter(member, post);
        post.addLikeCount();
    }

    public void dislikePost(Member member, Long postId) {
        Post post = findPostById(postId);
        checkNotWriter(member, post);
        post.deductLikeCount();
    }

    public void deletePost(Member member, Long postId) {
        Post post = findPostById(postId);
        checkWriterOrAdmin(member, post);
        post.setIsDeleted();
        post.getBoard().deductPostCount();
    }

    private void checkNotWriter(Member member, Post post) {
        if (post.getMember().equals(member)) throw new UnauthorizedException("자신의 게시글을 좋아할 수 없습니다.");
    }
}
