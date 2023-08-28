package org.poolc.api.board.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.poolc.api.member.domain.MemberRole;

@Getter
public class BoardUpdateRequest {
    private final String urlPath;
    private final MemberRole readPermission;
    private final MemberRole writePermission;

    @JsonCreator
    public BoardUpdateRequest(String urlPath, MemberRole readPermission, MemberRole writePermission) {
        this.urlPath = urlPath;
        this.readPermission = readPermission;
        this.writePermission = writePermission;
    }
}
