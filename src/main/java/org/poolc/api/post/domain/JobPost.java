package org.poolc.api.post.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;


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

    public JobPost() {}

    public JobPost(JobType position, String region, String field, LocalDateTime deadline) {
        this.position = position;
        this.region = region;
        this.field = field;
        this.deadline = deadline;
    }

}
