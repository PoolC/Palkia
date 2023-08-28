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
    private final MemberService memberService;

    public void like(String memberId, Subject subject, Long subjectId) {
        Member member = memberService.getMemberByLoginID(memberId);
        if (checkIfLiked(memberId, subject, subjectId)) {
            Long likeId = likeRepository.findByMemberIdAndSubjectAndSubjectId(memberId, subject, subjectId)
                    .get()
                    .getSubjectId();
            likeRepository.deleteById(likeId);
            deductLikeToSubject(member, subject, subjectId);
        } else {
            likeRepository.save(new Like(memberId, subject, subjectId));
            addLikeToSubject(member, subject, subjectId);
        }
    }

    private boolean checkIfLiked(String memberId, Subject subject, Long subjectId) {
        return likeRepository.existsByMemberIdAndSubjectAndSubjectId(memberId, subject, subjectId);
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
