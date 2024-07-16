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

    @Transactional
    public void like(Member member, Subject subject, Long subjectId) {
        if (checkIfSelf(member, subject, subjectId)) throw new IllegalArgumentException("자신의 글에는 좋아요를 누를 수 없습니다.");
        if (checkIfLiked(member.getLoginID(), subject, subjectId)) {
            // 이미 if condition으로 null 여부 확인 완료한 상태
            Long likeId = likeRepository.findByLoginIDAndSubjectAndSubjectId(member.getLoginID(), subject, subjectId).get().getId();
            likeRepository.deleteById(likeId);
            deductLikeToSubject(member, subject, subjectId);
        } else {
            likeRepository.save(new Like(member.getLoginID(), subject, subjectId));
            addLikeToSubject(member, subject, subjectId);
        }
    }

    private boolean checkIfSelf(Member member, Subject subject, Long subjectId) {
        switch (subject) {
            case COMMENT:
                return commentService.findById(subjectId).getMember().equals(member);
            case POST:
                return postService.findById(member, subjectId).getMember().equals(member);
            default:
                return false;
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
