package org.poolc.api.post.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.poolc.api.comment.domain.Comment;
import org.poolc.api.common.domain.TimestampEntity;
import org.poolc.api.member.domain.Member;
import org.poolc.api.post.vo.PostCreateValues;
import org.poolc.api.post.vo.PostUpdateValues;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.EAGER;

@Getter
@Entity
@SequenceGenerator(
        name = "POST_SEQ_GENERATOR",
        sequenceName = "POST_SEQ"
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Post extends TimestampEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POST_SEQ_GENERATOR")
    private Long id;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_uuid", nullable = false, referencedColumnName = "UUID")
    private Member member;

    @Column(name = "anonymous", nullable = false, columnDefinition = "boolean default true")
    private Boolean anonymous;

    @Column(name = "title", nullable = false, columnDefinition = "char(255)")
    private String title;

    @Column(name = "body", nullable = false, columnDefinition = "text")
    private String body;

    @ElementCollection(fetch = EAGER)
    @CollectionTable(name = "post_file_list", joinColumns = @JoinColumn(name = "post_id"), uniqueConstraints = {@UniqueConstraint(columnNames = {"post_id", "file_uri"})})
    @Column(name = "file_uri", columnDefinition = "varchar(1024)")
    private List<String> fileList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Comment> commentList = new ArrayList<>();

    @Column(name = "post_type")
    @Enumerated
    private PostType postType = PostType.GENERAL_POST;

    // 질문이면 수정 삭제 방지
    @Column(name = "is_question", columnDefinition = "boolean default false")
    private Boolean isQuestion = false;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "boolean default false")
    private Boolean isDeleted = false;

    @Column(name = "like_count", columnDefinition = "bigint default 0")
    private Long likeCount = 0L;

    @Column(name = "scrap_count", columnDefinition = "bigint default 0")
    private Long scrapCount = 0L;

    @Column(name = "comment_count", columnDefinition = "bigint default 0")
    private Long commentCount = 0L;

    @Column(name = "position")
    @Enumerated(EnumType.ORDINAL)
    private JobType position;

    @Column(name = "region", columnDefinition = "char(20)")
    private String region;

    @Column(name = "field", columnDefinition = "char(20)")
    private String field;

    @Column(name = "deadline")
    private LocalDateTime deadline;

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

    protected Post() {}

    public Post(BoardType boardType, Member member, Boolean anonymous, String title, String body, List<String> fileList, List<Comment> commentList, PostType postType, Boolean isQuestion, Boolean isDeleted, Long likeCount, Long scrapCount, Long commentCount, JobType position, String region, String field, LocalDateTime deadline) {
        this.boardType = boardType;
        this.member = member;
        this.anonymous = anonymous;
        this.title = title;
        this.body = body;
        this.fileList = fileList;
        this.commentList = commentList;
        this.postType = postType;
        this.isQuestion = isQuestion;
        this.isDeleted = isDeleted;
        this.likeCount = likeCount;
        this.scrapCount = scrapCount;
        this.commentCount = commentCount;
        this.position = position;
        this.region = region;
        this.field = field;
        this.deadline = deadline;
    }

    public Post(Member member, PostCreateValues values) {
        this.boardType = values.getBoardType();
        this.member = member;
        this.anonymous = values.getAnonymous();
        this.title = values.getTitle();
        this.body = values.getBody();
        this.fileList = values.getFileList();
        this.commentList = values.getCommentList();
        this.postType = values.getPostType();
        this.isDeleted = false;
        this.likeCount = 0L;
        this.scrapCount = 0L;
        this.commentCount = 0L;
        if (values.getPostType() == PostType.JOB_POST) {
            this.position = values.getPosition();
            this.region = values.getRegion();
            this.field = values.getField();
            this.deadline = values.getDeadline();
        } else {
            this.isQuestion = values.getIsQuestion();
        }
    }

    public void setIsDeleted() {
        this.isDeleted = true;
    }

    public void updatePost(PostType postType, PostUpdateValues values) {
        this.anonymous = values.getAnonymous();
        this.title = values.getTitle();
        this.body = values.getBody();
        this.fileList = values.getFileList();
        if (postType == PostType.JOB_POST) {
            this.position = values.getPosition();
            this.region = values.getRegion();
            this.field = values.getField();
            this.deadline = values.getDeadline();
        }
    }
}
