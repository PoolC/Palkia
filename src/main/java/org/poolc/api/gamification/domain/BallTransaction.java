package org.poolc.api.gamification.domain;

import lombok.Getter;
import org.poolc.api.member.domain.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "ball_transaction")
@SequenceGenerator(name = "BALL_TRANSACTION_SEQ", sequenceName = "BALL_TRANSACTION_SEQ")
public class BallTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BALL_TRANSACTION_SEQ")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_uuid", nullable = false, referencedColumnName = "UUID")
    private Member member;

    @Column(nullable = false)
    private int amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private BallTransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "ball_type", nullable = false, length = 20)
    private BallType ballType;

    @Column(name = "source_type", nullable = false, length = 40)
    private String sourceType;

    @Column(name = "source_id", nullable = false, length = 80)
    private String sourceId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected BallTransaction() {
    }

    public BallTransaction(Member member, int amount, BallTransactionType type, BallType ballType, String sourceType, String sourceId) {
        this.member = member;
        this.amount = amount;
        this.type = type;
        this.ballType = ballType;
        this.sourceType = sourceType;
        this.sourceId = sourceId;
        this.createdAt = LocalDateTime.now();
    }
}
