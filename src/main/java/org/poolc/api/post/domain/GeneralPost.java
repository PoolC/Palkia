package org.poolc.api.post.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.poolc.api.comment.domain.Comment;

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

}
