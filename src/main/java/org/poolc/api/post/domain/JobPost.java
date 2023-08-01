package org.poolc.api.post.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.poolc.api.board.domain.Board;
import org.poolc.api.member.domain.Member;


import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@SequenceGenerator(
        name = "JOB_POST_GENERATOR",
        sequenceName = "JOB_POST_SEQ"
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobPost extends Post {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JOB_POST_SEQ_GENERATOR")
    private Long id;

    @Column(name = "position", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private JobType position;

    @Column(name = "region", nullable = false, columnDefinition = "char(20)")
    private String region;

    @Column(name = "field", nullable = false, columnDefinition = "char(20)")
    private String field;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    public JobPost() {
        super.setPostType(PostType.JOB_POST);
    }

    public JobPost(Long id, JobType position, String region, String field, LocalDateTime deadline) {
        this.id = id;
        this.position = position;
        this.region = region;
        this.field = field;
        this.deadline = deadline;
        super.setPostType(PostType.JOB_POST);
    }

    public JobPost(Board board, Member member, Boolean anonymous, String title, String body, Long id, JobType position, String region, String field, LocalDateTime deadline) {
        super(board, member, anonymous, title, body);
        this.id = id;
        this.position = position;
        this.region = region;
        this.field = field;
        this.deadline = deadline;
        super.setPostType(PostType.JOB_POST);
    }

}
