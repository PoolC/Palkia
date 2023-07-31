package org.poolc.api.comment.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.poolc.api.member.domain.Member;
import org.poolc.api.post.domain.JobPost;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "Reply")
@SequenceGenerator(
        name = "REPLY_SEQ_GENERATOR",
        sequenceName = "REPLY_SEQ"
)
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Reply {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REPLY_SEQ_GENERATOR")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "job_post_id", nullable = false, referencedColumnName = "ID")
    private JobPost jobPost;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "author_uuid", nullable = false, referencedColumnName = "UUID")
    private Member member;

    @Column(name = "anonymous", nullable = false)
    private Boolean anonymous = false;

    @Column(name = "body", nullable = false)
    private String body;

    @Column(name = "like_count", columnDefinition = "bigint default 0")
    private Long likeCount;

    public Reply() {}

    public Reply(JobPost jobPost, Member member, Boolean anonymous, String body, Long likeCount) {
        this.jobPost = jobPost;
        this.member = member;
        this.anonymous = anonymous;
        this.body = body;
        this.likeCount = likeCount;
    }

    public void addLikeCount() {
        this.likeCount ++;
    }

    public void deductLikeCount() {
        this.likeCount --;
    }

}
