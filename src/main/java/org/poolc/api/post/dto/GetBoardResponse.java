package org.poolc.api.post.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.List;

@Getter
public class GetBoardResponse {
    private final int maxPage;
    private final List<GetPostsResponse> posts;

    @JsonCreator
    public GetBoardResponse(int maxPage, List<GetPostsResponse> posts) {
        this.maxPage = maxPage;
        this.posts = posts;
    }
}
