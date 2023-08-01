package org.poolc.api.comment.dto;

import lombok.Builder;
import lombok.Getter;
import org.poolc.api.comment.domain.Reply;

@Getter
@Builder
public class ReplyResponse {
    private final Long replyId;
    private final Long questionPostId;
    private final String writerLoginId;
    private final String writerName;
    private final Boolean anonymous;
    private final String body;
    private final Boolean isDeleted;
    private final Long likeCount;

    public static ReplyResponse of(Reply reply) {
        if (reply.getIsDeleted()) {
            return null;
        } else if (reply.getAnonymous()) {
            return ReplyResponse.builder()
                    .replyId(reply.getId())
                    .questionPostId(reply.getQuestionPost().getId())
                    .writerLoginId(null)
                    .writerName(null)
                    .anonymous(true)
                    .body(reply.getBody())
                    .isDeleted(false)
                    .likeCount(reply.getLikeCount())
                    .build();
        } else {
            return ReplyResponse.builder()
                    .replyId(reply.getId())
                    .questionPostId(reply.getQuestionPost().getId())
                    .writerLoginId(reply.getMember().getLoginID())
                    .writerName(reply.getMember().getName())
                    .anonymous(false)
                    .body(reply.getBody())
                    .isDeleted(false)
                    .likeCount(reply.getLikeCount())
                    .build();
        }
    }
}
