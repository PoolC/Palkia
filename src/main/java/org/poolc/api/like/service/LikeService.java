package org.poolc.api.like.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.comment.domain.Comment;
import org.poolc.api.comment.repository.CommentRepository;
import org.poolc.api.comment.service.CommentService;
import org.poolc.api.like.domain.Like;
import org.poolc.api.like.domain.Subject;
import org.poolc.api.like.repository.LikeRepository;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.service.MemberService;
import org.poolc.api.post.domain.Post;
import org.poolc.api.post.service.PostService;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostService postService;
    private final CommentService commentService;

    public void like(Member member, Subject subject, Long subjectId) {
        Comment comment = commentService.findById(subjectId);
        if (member.equals(comment.getMember())) {
            throw new IllegalArgumentException("본인의 댓글은 좋아할 수 없습니다.");
        }

        if (checkIfLiked(member.getLoginID(), subject, subjectId)) {
            Long likeId = likeRepository.findByLoginIDAndSubjectAndSubjectId(member.getLoginID(), subject, subjectId)
                    .orElseThrow(() -> new NoSuchElementException("해당하는 좋아요가 없습니다.")).getId();
            likeRepository.deleteById(likeId);
            deductLikeToSubject(member, subject, subjectId);
        } else {
            likeRepository.save(new Like(member.getLoginID(), subject, subjectId));
            addLikeToSubject(member, subject, subjectId);
        }
    }

    private boolean checkIfLiked(String loginID, Subject subject, Long subjectId) {
        return likeRepository.existsByLoginIDAndSubjectAndSubjectId(loginID, subject, subjectId);
    }

    private void addLikeToSubject(Member member, Subject subject, Long subjectId) {
        if (subject == Subject.COMMENT) {
            commentService.likeComment(member, subjectId);
        } else {
            postService.likePost(member, subjectId);
        }
    }
    private void deductLikeToSubject(Member member, Subject subject, Long subjectId) {
        if (subject == Subject.COMMENT) {
            commentService.dislikeComment(member, subjectId);
        } else {
            postService.dislikePost(member, subjectId);
        }
    }
}
