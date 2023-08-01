package org.poolc.api.post.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.poolc.api.board.domain.Board;
import org.poolc.api.comment.domain.Reply;
import org.poolc.api.member.domain.Member;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SequenceGenerator(
        name = "QUESTION_POST_GENERATOR",
        sequenceName = "QUESTION_POST_SEQ"
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionPost extends Post {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QUESTION_POST_SEQ_GENERATOR")
    private Long id;

    @OneToMany(mappedBy = "questionPost", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Reply> replyList = new ArrayList<>();

    public QuestionPost() {}

    public QuestionPost(Long id, List<Reply> replyList) {
        this.id = id;
        this.replyList = replyList;
    }

    public QuestionPost(Board board, Member member, Boolean anonymous, String title, String body, Long id, List<Reply> replyList) {
        super(board, member, anonymous, title, body);
        this.id = id;
        this.replyList = replyList;
    }
}
