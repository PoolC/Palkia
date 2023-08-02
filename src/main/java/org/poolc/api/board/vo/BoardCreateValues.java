package org.poolc.api.board.vo;

import lombok.Getter;
import org.poolc.api.board.dto.BoardCreateRequest;
import org.poolc.api.member.domain.MemberRole;

@Getter
public class BoardCreateValues {
    private final String name;
    private final String urlPath;
    private final MemberRole readPermission;
    private final MemberRole writePermission;

    public BoardCreateValues(BoardCreateRequest boardCreateRequest) {
        this.name = boardCreateRequest.getName();
        this.urlPath = boardCreateRequest.getUrlPath();
        this.readPermission = boardCreateRequest.getReadPermission();
        this.writePermission = boardCreateRequest.getWritePermission();
    }
}
