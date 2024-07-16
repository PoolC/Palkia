package org.poolc.api.like.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity(name = "USER_LIKE")
@SequenceGenerator(
        name = "LIKE_SEQ_GENERATOR",
        sequenceName = "LIKE_SEQ"
)
@Getter
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LIKE_SEQ_GENERATOR")
    @Column(name = "id")
    private Long id;

    @Column(name = "member_id", nullable = false)
    private String memberId;

    @Column(name = "subject", nullable = false)
    private Subject subject;

    @Column(name = "subject_id", nullable = false)
    private Long subjectId;

    protected Like() {}

    public Like(String loginID, Subject subject, Long subjectId) {
        this.loginID = loginID;
        this.subject = subject;
        this.subjectId = subjectId;
    }
}
