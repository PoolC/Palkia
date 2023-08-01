package org.poolc.api.post.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.poolc.api.board.domain.Board;
import org.poolc.api.common.domain.TimestampEntity;
import org.poolc.api.member.domain.Member;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.EAGER;

@Getter
@MappedSuperclass
public abstract class Post extends TimestampEntity {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false, referencedColumnName = "ID")
    private Board board;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_uuid", nullable = false, referencedColumnName = "UUID")
    private Member member;

    @Column(name = "anonymous", nullable = false)
    private Boolean anonymous = false;

    @Column(name = "title", nullable = false, columnDefinition = "char(255)")
    private String title;

    @Column(name = "body", nullable = false, columnDefinition = "text")
    private String body;

    @ElementCollection(fetch = EAGER)
    @CollectionTable(name = "post_file_list", joinColumns = @JoinColumn(name = "post_id"), uniqueConstraints = {@UniqueConstraint(columnNames = {"post_id", "file_uri"})})
    @Column(name = "file_uri", columnDefinition = "varchar(1024)")
    private List<String> fileList = new ArrayList<>();

    @Column(name = "post_type", nullable = false)
    private PostType postType;

    @Column(name = "like_count", columnDefinition = "bigint default 0")
    private Long likeCount;

    @Column(name = "scrap_count", columnDefinition = "bigint default 0")
    private Long scrapCount;

    @Column(name = "comment_count", columnDefinition = "bigint default 0")
    private Long commentCount;

    public void addLikeCount() {
        this.likeCount ++;
    }

    public void deductLikeCount() { this.likeCount --; }

    public void addScrapCount() {
        this.scrapCount ++;
    }

    public void deductScrapCount() { this.scrapCount --; }

    public void addCommentCount() { this.commentCount ++; }

    public void deductCommentCount() { this.commentCount --; }

    public Post() {}

    public Post(Board board, Member member, Boolean anonymous, String title, String body) {
        this.board = board;
        this.member = member;
        this.anonymous = anonymous;
        this.title = title;
        this.body = body;
    }

    public void setPostType(PostType postType) {
        this.postType = postType;
    }
}
