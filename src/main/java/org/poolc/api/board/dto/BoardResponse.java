package org.poolc.api.board.dto;

import lombok.Builder;
import lombok.Getter;
import org.poolc.api.board.domain.Board;
import org.poolc.api.member.domain.MemberRole;

@Getter
@Builder
public class BoardResponse {
    private Long boardId;
    private String name;
    private String urlPath;
    private Long postCount;
    private MemberRole readPermission;
    private MemberRole writePermission;

    public static BoardResponse of(Board board) {
        return BoardResponse.builder()
                .boardId(board.getId())
                .name(board.getName())
                .urlPath(board.getUrlPath())
                .postCount(board.getPostCount())
                .readPermission(board.getReadPermission())
                .writePermission(board.getWritePermission())
                .build();
    }
}
