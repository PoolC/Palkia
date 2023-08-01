package org.poolc.api.post.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.poolc.api.board.domain.Board;
import org.poolc.api.comment.domain.Comment;
import org.poolc.api.member.domain.Member;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "GENERAL_POST")
@SequenceGenerator(
        name = "GENERAL_POST_SEQ_GENERATOR",
        sequenceName = "GENERAL_POST_SEQ"
)
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeneralPost extends Post {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GENERAL_POST_SEQ_GENERATOR")
    private Long id;

    @OneToMany(mappedBy = "generalPost", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Comment> commentList = new ArrayList<>();

    public GeneralPost() {}

    public GeneralPost(Long id, List<Comment> commentList) {
        this.id = id;
        this.commentList = commentList;
    }

    public GeneralPost(Board board, Member member, Boolean anonymous, String title, String body, Long id, List<Comment> commentList) {
        super(board, member, anonymous, title, body);
        this.id = id;
        this.commentList = commentList;
    }
}
