package org.poolc.api.post.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.poolc.api.comment.domain.Reply;

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
public class QuestionPost {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QUESTION_POST_SEQ_GENERATOR")
    private Long id;

    @OneToMany(mappedBy = "questionPost", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Reply> replyList = new ArrayList<>();

    public QuestionPost() {}
}
