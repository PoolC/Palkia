package org.poolc.api.comment.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import org.poolc.api.common.domain.TimestampEntity;
import org.poolc.api.member.domain.Member;
import org.poolc.api.post.domain.QuestionPost;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "Reply")
@SequenceGenerator(
        name = "REPLY_SEQ_GENERATOR",
        sequenceName = "REPLY_SEQ"
)
@Getter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Reply extends TimestampEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REPLY_SEQ_GENERATOR")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "question_post_id", nullable = false, referencedColumnName = "ID")
    private QuestionPost questionPost;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "author_uuid", nullable = false, referencedColumnName = "UUID")
    private Member member;

    @Column(name = "anonymous", nullable = false)
    private Boolean anonymous = false;

    @Column(name = "body", nullable = false)
    private String body;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "like_count", columnDefinition = "bigint default 0")
    private Long likeCount;

    public Reply() {}

    public Reply(Long id, QuestionPost questionPost, Member member, Boolean anonymous, String body, Boolean isDeleted, Long likeCount) {
        this.id = id;
        this.questionPost = questionPost;
        this.member = member;
        this.anonymous = anonymous;
        this.body = body;
        this.isDeleted = isDeleted;
        this.likeCount = likeCount;
    }

    public Reply(QuestionPost questionPost, Member member, Boolean anonymous, String body, Boolean isDeleted, Long likeCount) {
        this.questionPost = questionPost;
        this.member = member;
        this.anonymous = anonymous;
        this.body = body;
        this.isDeleted = isDeleted;
        this.likeCount = likeCount;
    }

    public void addLikeCount() {
        this.likeCount ++;
    }

    public void deductLikeCount() {
        this.likeCount --;
    }

    public void setIsDeleted() { this.isDeleted = true; }

}
