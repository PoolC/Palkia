package org.poolc.api.scrap.domain;

import lombok.Getter;
import org.poolc.api.common.domain.TimestampEntity;

import javax.persistence.*;

@Getter
@Entity(name = "SCRAP")
@SequenceGenerator(
        name = "SCRAP_SEQ_GENERATOR",
        sequenceName = "SCRAP_SEQ"
)
public class Scrap extends TimestampEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SCRAP_SEQ_GENERATOR")
    private Long id;

    @Column(name = "member_id")
    private String memberId;

    @Column(name = "post_id")
    private Long postId;

    protected Scrap() {}

    public Scrap(String memberId, Long postId) {
        this.memberId = memberId;
        this.postId = postId;
    }
}
