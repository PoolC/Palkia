package org.poolc.api.post.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.poolc.api.comment.domain.Reply;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SequenceGenerator(
        name = "JOB_POST_GENERATOR",
        sequenceName = "JOB_SEQ"
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

    @OneToMany(mappedBy = "jobPost", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Reply> replyList = new ArrayList<>();

    public JobPost() {}

    public JobPost(JobType position, String region, String field, LocalDateTime deadline) {
        this.position = position;
        this.region = region;
        this.field = field;
        this.deadline = deadline;
    }

}
