package org.poolc.api.comment.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.comment.domain.Comment;
import org.poolc.api.comment.dto.CommentResponse;
import org.poolc.api.comment.repository.CommentRepository;
import org.poolc.api.comment.vo.CommentCreateValues;
import org.poolc.api.post.domain.GeneralPost;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    public CommentResponse createComment(CommentCreateValues values) {
        Comment comment = Comment.builder()
                .generalPost(values.getGeneralPost())
                .member(values.getMember())
                .anonymous(values.getAnonymous())
                .body(values.getBody())
                .isDeleted(false)
                .isChild(false)
                .parent(null)
                .build();
        commentRepository.save(comment);
        return CommentResponse.of(comment);
    }

    public CommentResponse createThread(CommentCreateValues values) {
        Comment comment = Comment.builder()
                .generalPost(values.getGeneralPost())
                .member(values.getMember())
                .anonymous(values.getAnonymous())
                .body(values.getBody())
                .isDeleted(false)
                .isChild(true)
                .parent(values.getParent())
                .build();
        commentRepository.save(comment);
        return CommentResponse.of(comment);
    }

    public List<Comment> findCommentsByGeneralPost(GeneralPost generalPost) {
        return commentRepository.findAllByGeneralPost(generalPost);
    }

    public List<Comment> findCommentsByParent(Long parentId) {
        Comment parent = commentRepository.findById(parentId)
                .orElseThrow(() -> new NoSuchElementException("No comment found with given comment id."));
        return commentRepository.findAllByParent(parent);
    }

    public Comment findOne(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("No comment found with given comment id."));
    }

    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("No comment found with given comment id."));
        comment.setIsDeleted();
    }
}
