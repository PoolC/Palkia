package org.poolc.api.comment.domain;

import lombok.Builder;
import lombok.Getter;
import org.poolc.api.comment.vo.CommentUpdateValues;
import org.poolc.api.common.domain.TimestampEntity;
import org.poolc.api.member.domain.Member;
import org.poolc.api.post.domain.Post;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "Comment")
@Builder
@Getter
public class Comment extends TimestampEntity {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "postID", referencedColumnName = "ID", nullable = false)
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "authorUUID", referencedColumnName = "UUID", nullable = false)
    private Member member;

    @Column(name = "body", columnDefinition = "text", nullable = false)
    private String body;

    public Comment(Long id, Post post, Member member, String body) {
        this.id = id;
        this.post = post;
        this.member = member;
        this.body = body;
    }

    public Comment() {
    }

    public Comment(Post post, Member member, String body) {
        this.post = post;
        this.member = member;
        this.body = body;
    }

    public void updateComment(CommentUpdateValues values) {
        this.body = values.getBody();
    }
}
