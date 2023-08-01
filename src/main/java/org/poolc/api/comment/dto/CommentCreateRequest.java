package org.poolc.api.comment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class CommentCreateRequest {
    private final Long generalPostId;
    private final Boolean anonymous;
    private final String body;
    private final Boolean isChild;
    private final Long parentId;

    @JsonCreator
    public CommentCreateRequest(Long generalPostId, Boolean anonymous, String body, Boolean isChild, Long parentId) {
        this.generalPostId = generalPostId;
        this.anonymous = anonymous;
        this.body = body;
        this.isChild = isChild;
        this.parentId = parentId;
    }
}
