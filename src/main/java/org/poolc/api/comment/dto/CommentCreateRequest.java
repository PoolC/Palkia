package org.poolc.api.comment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
public class CommentCreateRequest {

    @NotNull
    private final Long postId;

    @NotNull
    private final Boolean anonymous;

    @NotEmpty
    private final String body;

    @NotNull
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
