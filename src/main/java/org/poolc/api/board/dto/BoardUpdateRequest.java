package org.poolc.api.board.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.poolc.api.member.domain.MemberRole;

@Getter
public class BoardUpdateRequest {
    private final String name;
    private final String urlPath;
    private final MemberRole readPermission;
    private final MemberRole writePermission;

    @JsonCreator
    public BoardUpdateRequest(String name, String urlPath, MemberRole readPermission, MemberRole writePermission) {
        this.name = name;
        this.urlPath = urlPath;
        this.readPermission = readPermission;
        this.writePermission = writePermission;
    }
}
