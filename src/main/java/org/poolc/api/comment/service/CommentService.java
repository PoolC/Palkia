package org.poolc.api.comment.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.comment.domain.Comment;
import org.poolc.api.comment.dto.CommentResponse;
import org.poolc.api.comment.repository.CommentRepository;
import org.poolc.api.comment.vo.CommentCreateValues;
import org.poolc.api.comment.vo.CommentUpdateValues;
import org.poolc.api.post.domain.Post;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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
            return comment;
        }
    }

    public List<Comment> findCommentsByPost(Post post) {
        return commentRepository.findAllByPost(post);
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

    public void deleteComment(Long commentId) {
        Comment comment = findById(commentId);
        comment.setIsDeleted();
    }

    public void updateComment(Long commentId, CommentUpdateValues commentUpdateValues) {
        Comment comment = findById(commentId);
        comment.updateComment(commentUpdateValues);
    }

    public void likeComment(Long commentId) {
        Comment comment = findById(commentId);
        if (comment.getPost().getIsQuestion()) comment.addLikeCount();
    }

    public void dislikeComment(Long commentId) {
        Comment comment = findById(commentId);
        if (comment.getPost().getIsQuestion()) comment.deductLikeCount();
    }
}
