package org.poolc.api.post.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.poolc.api.post.domain.PostType;

import java.util.List;

/* Create request for General & Question Post*/

@Getter
public class PostCreateRequest {
    private final Long boardId;
    private final Boolean anonymous;
    private final String title;
    private final String body;
    private final List<String> fileList;
    private final PostType postType;

    @JsonCreator
    public PostCreateRequest(Long boardId, Boolean anonymous, String title, String body, List<String> fileList, PostType postType) {
        this.boardId = boardId;
        this.anonymous = anonymous;
        this.title = title;
        this.body = body;
        this.fileList = fileList;
        this.postType = postType;
    }

}
