package org.poolc.api.board.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.poolc.api.board.domain.BoardName;
import org.poolc.api.member.domain.MemberRole;

@Getter
public class BoardCreateRequest {
    private final BoardName boardName;
    private final String urlPath;
    private final MemberRole readPermission;
    private final MemberRole writePermission;

    @JsonCreator
    public BoardCreateRequest(BoardName boardName, String urlPath, MemberRole readPermission, MemberRole writePermission) {
        this.boardName = boardName;
        this.urlPath = urlPath;
        this.readPermission = readPermission;
        this.writePermission = writePermission;
    }
}
