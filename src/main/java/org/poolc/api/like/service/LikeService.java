package org.poolc.api.like.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.like.domain.Like;
import org.poolc.api.like.domain.Subject;
import org.poolc.api.like.repository.LikeRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;

    public void like(String memberId, Subject subject, Long subjectId) {
        if (checkIfLiked(memberId, subject, subjectId)) {
            Long likeId = likeRepository.findByMemberIdAndSubjectAndSubjectId(memberId, subject, subjectId).get().getSubjectId();
            likeRepository.deleteById(likeId);
        } else {
            likeRepository.save(new Like(memberId, subject, subjectId));
        }
    }

    public boolean checkIfLiked(String memberId, Subject subject, Long subjectId) {
        return likeRepository.existsByMemberIdAndSubjectAndSubjectId(memberId, subject, subjectId);
    }
}
