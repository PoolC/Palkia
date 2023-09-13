package org.poolc.api.post.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.auth.exception.UnauthorizedException;
import org.poolc.api.comment.domain.Comment;
import org.poolc.api.member.domain.Member;
import org.poolc.api.post.domain.BoardType;
import org.poolc.api.post.domain.Post;
import org.poolc.api.post.dto.PostResponse;
import org.poolc.api.post.repository.PostRepository;
import org.poolc.api.post.vo.PostCreateValues;
import org.poolc.api.post.vo.PostUpdateValues;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private static final int size = 15;

    public Post findPostById(Member member, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("No post found with given post id."));
        checkReadPermission(member, post.getBoardType());
        post.getCommentList().sort(Comparator.comparing(Comment::getCreatedAt));
        return post;
    }

    @Transactional(readOnly = true)
    public List<PostResponse> findPostsByMember(Member member, int page) {
        PageRequest pr = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findByMember(member, pr);
        if (posts.getNumberOfElements() == 0) return null;
        return posts.stream()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .map(PostResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> findPostsByBoard(Member member, BoardType boardType, int page) {
        checkReadPermission(member, boardType);
        PageRequest pr = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findByBoardType(boardType, pr);
        if (posts.getNumberOfElements() == 0) return null;
        return posts.stream()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .map(PostResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void createPost(PostCreateValues values) {
        checkWritePermission(values.getMember(), values.getBoardType());
        Post post = new Post(values.getMember(), values);
        System.out.println(post.getTitle());
        postRepository.save(post);
        BoardType.addPostCount(values.getBoardType());
    }

    @Transactional
    public void updatePost(Member member, Long postId, PostUpdateValues values) {
        Post post = findPostById(member, postId);
        checkWriter(member, post);
        post.updatePost(post.getPostType(), values);
    }

    public void likePost(Member member, Long postId) {
        Post post = findPostById(member, postId);
        checkNotWriter(member, post);
        post.addLikeCount();
    }

    public void dislikePost(Member member, Long postId) {
        Post post = findPostById(member, postId);
        checkNotWriter(member, post);
        post.deductLikeCount();
    }

    public void scrapPost(Member member, Long postId) {
        Post post = findPostById(member, postId);
        checkNotWriter(member, post);
        post.addScrapCount();
    }

    public void unscrapPost(Member member, Long postId) {
        Post post = findPostById(member, postId);
        checkNotWriter(member, post);
        post.deductScrapCount();
    }

    public void deletePost(Member member, Long postId) {
        Post post = findPostById(member, postId);
        checkWriterOrAdmin(member, post);
        if (!post.getIsQuestion()) {
            post.setIsDeleted();
        }
        BoardType.removePostCount(post.getBoardType());
    }

    public List<PostResponse> searchPost(Member member, String keyword, int page) {
        PageRequest pr = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findByTitleContainingOrBodyContaining(keyword, keyword, pr);
        return posts.stream()
                .filter(post -> checkReadPermissionBoolean(member, post.getBoardType()))
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .map(PostResponse::of)
                .collect(Collectors.toList());
    }


    private void checkWritePermission(Member member, BoardType board) {
        if (!checkWritePermissionBoolean(member, board)) throw new UnauthorizedException("접근할 수 없습니다.");
        else if(board == BoardType.NOTICE && !member.isAdmin()) throw new UnauthorizedException("임원진만 접근할 수 있습니다.");
    }
    private void checkReadPermission(Member user, BoardType board) {
        if (user == null && board != BoardType.NOTICE) {
            throw new UnauthorizedException("접근할 수 없습니다.");
        }
    }

    private boolean checkReadPermissionBoolean(Member user, BoardType board) {
        return user != null || board == BoardType.NOTICE;
    }

    private boolean checkWritePermissionBoolean(Member user, BoardType board) {
        if (user == null || !user.isMember() || (!user.isAdmin() && board == BoardType.NOTICE)) return false;
        return true;
    }

    private void checkWriter(Member member, Post post) {
        if (!post.getMember().equals(member)) throw new UnauthorizedException("접근할 수 없습니다.");
    }

    private void checkWriterOrAdmin(Member user, Post post) {
        if (!post.getMember().equals(user) && !user.isAdmin()) throw new UnauthorizedException("접근할 수 없습니다.");
    }

    private void checkNotWriter(Member member, Post post) {
        if (post.getMember().equals(member)) throw new UnauthorizedException("자신의 게시글을 좋아할 수 없습니다.");
    }
}
