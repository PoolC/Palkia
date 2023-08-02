package org.poolc.api.comment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class CommentCreateRequest {
    private final Long postId;
    private final Boolean anonymous;
    private final String body;
    private final Boolean isChild;
    private final Long parentId;

    @JsonCreator
    public CommentCreateRequest(Long postId, Boolean anonymous, String body, Boolean isChild, Long parentId) {
        this.postId = postId;
        this.anonymous = anonymous;
        this.body = body;
        this.isChild = isChild;
        this.parentId = parentId;
    }
}
