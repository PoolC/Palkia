package org.poolc.api.comment.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.comment.domain.Reply;
import org.poolc.api.comment.dto.ReplyResponse;
import org.poolc.api.comment.repository.ReplyRepository;
import org.poolc.api.comment.vo.ReplyCreateValues;
import org.poolc.api.post.domain.QuestionPost;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReplyService {
    private final ReplyRepository replyRepository;

    public ReplyResponse createReply(ReplyCreateValues values) {
        Reply reply = Reply.builder()
                .questionPost(values.getQuestionPost())
                .member(values.getMember())
                .anonymous(values.getAnonymous())
                .body(values.getBody())
                .isDeleted(false)
                .likeCount(0L)
                .build();
        replyRepository.save(reply);
        return ReplyResponse.of(reply);
    }

    public List<Reply> findRepliesByQuestionPost(QuestionPost questionPost) {
        return replyRepository.findAllByQuestionPost(questionPost);
    }

    public Reply findOne(Long replyId) {
        return replyRepository.findById(replyId)
                .orElseThrow(() -> new NoSuchElementException("No reply found with given reply id."));
    }

    public void deleteReply(Long replyId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new NoSuchElementException("No reply found with given reply id."));
        reply.setIsDeleted();
    }

    public void likeReply(Long replyId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new NoSuchElementException("No reply found with given reply id."));
        reply.addLikeCount();
    }
    public void dislikeReply(Long replyId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new NoSuchElementException("No reply found with given reply id."));
        reply.deductLikeCount();
    }
}
