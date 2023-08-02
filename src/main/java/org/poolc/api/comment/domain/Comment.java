package org.poolc.api.comment.domain;

import lombok.Builder;
import lombok.Getter;
import org.poolc.api.comment.vo.CommentUpdateValues;
import org.poolc.api.common.domain.TimestampEntity;
import org.poolc.api.member.domain.Member;
import org.poolc.api.post.domain.Post;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "Comment")
@SequenceGenerator(
        name = "COMMENT_SEQ_GENERATOR",
        sequenceName = "COMMENT_SEQ"
)
@Getter
@Builder
public class Comment extends TimestampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMENT_SEQ_GENERATOR")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id", nullable = false, referencedColumnName = "ID")
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "author_uuid", nullable = false, referencedColumnName = "UUID")
    private Member member;

    @Column(name = "anonymous", nullable = false, columnDefinition = "boolean default false")
    private Boolean anonymous;

    @Column(name = "body", nullable = false, columnDefinition = "text")
    private String body;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "boolean default false")
    private Boolean isDeleted;

    // 대댓글이면 children 가질 수 없음
    @Column(name = "is_child", nullable = false)
    private Boolean isChild;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "ID")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    @Builder.Default
    private List<Comment> children = new ArrayList<>();

    @Column(name = "like_count")
    private Long likeCount;

    public Comment() {}

    public Comment(Post post, Member member, Boolean anonymous, String body, Boolean isDeleted, Boolean isChild, Comment parent, List<Comment> children, Long likeCount) {
        this.post = post;
        this.member = member;
        this.anonymous = anonymous;
        this.body = body;
        this.isDeleted = isDeleted;
        this.isChild = isChild;
        this.parent = parent;
        this.children = children;
        this.likeCount = likeCount;
    }

    public void setIsDeleted() {
        this.isDeleted = true;
    }
    public boolean hasChildren() { return this.getChildren().size() != 0; }
    public void updateComment(CommentUpdateValues commentUpdateValues) {
        this.anonymous = commentUpdateValues.getAnonymous();
        this.body = commentUpdateValues.getBody();
    }
    public void addLikeCount() {
        if (this.post.getIsQuestion()) likeCount ++;
    }
    public void deductLikeCount() {
        if (this.post.getIsQuestion()) likeCount --;
    }
}
