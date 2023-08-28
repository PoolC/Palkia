package org.poolc.api.board.vo;

import lombok.Builder;
import lombok.Getter;
import org.poolc.api.board.domain.BoardName;
import org.poolc.api.board.dto.BoardUpdateRequest;
import org.poolc.api.member.domain.MemberRole;

@Getter
public class BoardUpdateValues {
    private final String urlPath;
    private final MemberRole readPermission;
    private final MemberRole writePermission;

    public BoardUpdateValues(BoardUpdateRequest boardUpdateRequest) {
        this.urlPath = boardUpdateRequest.getUrlPath();
        this.readPermission = boardUpdateRequest.getReadPermission();
        this.writePermission = boardUpdateRequest.getWritePermission();
    }
}
