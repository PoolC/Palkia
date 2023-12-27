package org.poolc.api.post.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.List;

@Getter
public class GetBoardResponse {
    private final int maxPage;
    private final List<PostResponse> posts;

    @JsonCreator
    public GetBoardResponse(int maxPage, List<PostResponse> posts) {
        this.maxPage = maxPage;
        this.posts = posts;
    }
}
