package org.poolc.api.comment.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.auth.exception.UnauthorizedException;
import org.poolc.api.comment.domain.Comment;
import org.poolc.api.comment.repository.CommentRepository;
import org.poolc.api.comment.vo.CommentCreateValues;
import org.poolc.api.comment.vo.CommentUpdateValues;
import org.poolc.api.member.domain.Member;
import org.poolc.api.post.domain.Post;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    public Comment createComment(CommentCreateValues values) {
        if (values.getParent().getIsChild()) {
            return null;
        } else {
            Comment comment = new Comment(values);
            commentRepository.save(comment);
            comment.getPost().addCommentCount();
            return comment;
        }
    }

    public List<Comment> findCommentsByPost(Post post) {
        return commentRepository.findAllByPost(post).stream()
                .sorted(Comparator.comparing(Comment::getCreatedAt))
                .collect(Collectors.toList());
    }

    public List<Comment> findCommentsByParent(Long parentId) {
        Comment parent = commentRepository.findById(parentId)
                .orElseThrow(() -> new NoSuchElementException("No comment found with given comment id."));
        return commentRepository.findAllByParent(parent);
    }

    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("No comment found with given comment id."));
    }

    public void deleteComment(Member member, Long commentId) {
        Comment comment = findById(commentId);
        checkWriterOrAdmin(member, comment);
        comment.setIsDeleted();
        comment.getPost().deductCommentCount();
    }

    public void updateComment(Member member, Long commentId, CommentUpdateValues commentUpdateValues) {
        Comment comment = findById(commentId);
        checkWriter(member, comment);
        comment.updateComment(commentUpdateValues);
    }

    public void likeComment(Member member, Long commentId) {
        Comment comment = findById(commentId);
        checkNotWriter(member, comment);
        if (comment.getPost().getIsQuestion()) comment.addLikeCount();
    }

    public void dislikeComment(Member member, Long commentId) {
        Comment comment = findById(commentId);
        checkNotWriter(member, comment);
        if (comment.getPost().getIsQuestion()) comment.deductLikeCount();
    }

    private void checkWriterOrAdmin(Member member, Comment comment) {
        if (!comment.getMember().equals(member) && !member.isAdmin()) throw new UnauthorizedException("접근할 수 없습니다.");
    }

    private void checkWriter(Member member, Comment comment) {
        if (!comment.getMember().equals(member)) throw new UnauthorizedException("접근할 수 없습니다.");
    }

    private void checkNotWriter(Member member, Comment comment) {
        if (comment.getMember().equals(member)) throw new UnauthorizedException("자신의 댓글을 좋아할 수 없습니다.");
    }
}
