package org.poolc.api.like.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.comment.domain.Comment;
import org.poolc.api.comment.repository.CommentRepository;
import org.poolc.api.like.domain.Like;
import org.poolc.api.like.domain.Subject;
import org.poolc.api.like.repository.LikeRepository;
import org.poolc.api.post.domain.Post;
import org.poolc.api.post.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public void like(String memberId, Subject subject, Long subjectId) {
        if (checkIfLiked(memberId, subject, subjectId)) {
            Long likeId = likeRepository.findByMemberIdAndSubjectAndSubjectId(memberId, subject, subjectId)
                    .get()
                    .getSubjectId();
            likeRepository.deleteById(likeId);
            deductLikeToSubject(subject, subjectId);
        } else {
            likeRepository.save(new Like(memberId, subject, subjectId));
            addLikeToSubject(subject, subjectId);
        }
    }

    private boolean checkIfLiked(String memberId, Subject subject, Long subjectId) {
        return likeRepository.existsByMemberIdAndSubjectAndSubjectId(memberId, subject, subjectId);
    }

    private void addLikeToSubject(Subject subject, Long subjectId) {
        if (subject == Subject.COMMENT) {
            Comment comment = commentRepository.findById(subjectId)
                    .orElseThrow(() -> new NoSuchElementException("No comment with given id."));
            comment.addLikeCount();
        } else {
            Post post = postRepository.findById(subjectId)
                    .orElseThrow(() -> new NoSuchElementException("No post with given id."));
            post.addLikeCount();
        }
    }
    private void deductLikeToSubject(Subject subject, Long subjectId) {
        if (subject == Subject.COMMENT) {
            Comment comment = commentRepository.findById(subjectId)
                    .orElseThrow(() -> new NoSuchElementException("No comment with given id."));
            comment.deductLikeCount();
        } else {
            Post post = postRepository.findById(subjectId)
                    .orElseThrow(() -> new NoSuchElementException("No post with given id."));
            post.deductLikeCount();
        }
    }
}
