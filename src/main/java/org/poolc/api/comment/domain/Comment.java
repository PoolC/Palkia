package org.poolc.api.comment.domain;

import lombok.Builder;
import lombok.Getter;
import org.poolc.api.member.domain.Member;
import org.poolc.api.post.domain.GeneralPost;

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
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMENT_SEQ_GENERATOR")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "general_post_id", nullable = false, referencedColumnName = "ID")
    private GeneralPost generalPost;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "author_uuid", nullable = false, referencedColumnName = "UUID")
    private Member member;

    @Column(name = "anonymous", nullable = false)
    private Boolean anonymous = false;

    @Column(name = "body", nullable = false, columnDefinition = "text")
    private String body;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    // 대댓글이면 children 가질 수 없음
    @Column(name = "is_child", nullable = false)
    private Boolean isChild;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "ID")
    private Comment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    public Comment() {}

    public Comment(Long id, GeneralPost generalPost, Member member, Boolean anonymous, String body, Boolean isDeleted, Boolean isChild, Comment parent, List<Comment> children) {
        this.id = id;
        this.generalPost = generalPost;
        this.member = member;
        this.anonymous = anonymous;
        this.body = body;
        this.isDeleted = isDeleted;
        this.isChild = isChild;
        this.parent = parent;
        this.children = children;
    }

    public Comment(GeneralPost generalPost, Member member, Boolean anonymous, String body, Boolean isDeleted, Boolean isChild, Comment parent, List<Comment> children) {
        this.generalPost = generalPost;
        this.member = member;
        this.anonymous = anonymous;
        this.body = body;
        this.isDeleted = isDeleted;
        this.isChild = isChild;
        this.parent = parent;
        this.children = children;
    }
}
