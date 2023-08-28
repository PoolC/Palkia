package org.poolc.api.board.domain;


import lombok.Getter;
import org.poolc.api.board.vo.BoardCreateValues;
import org.poolc.api.board.vo.BoardUpdateValues;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.domain.MemberRole;
import org.poolc.api.member.domain.MemberRoles;

import javax.persistence.*;

@Entity(name = "BOARD")
@SequenceGenerator(
        name = "BOARD_SEQ_GENERATOR",
        sequenceName = "BOARD_SEQ"
)
@Getter
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOARD_SEQ_GENERATOR")
    @Column(name = "id")
    private Long id;

    @Column(name = "board_name", unique = true, nullable = false)
    private BoardName boardName;

    @Column(name = "url_path", unique = true, nullable = false, columnDefinition = "varchar(40)")
    private String urlPath;

    @Column(name = "post_count", columnDefinition = "bigint default 0")
    private Long postCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "read_permission", nullable = false, columnDefinition = "varchar(10)")
    private MemberRole readPermission;

    @Enumerated(EnumType.STRING)
    @Column(name = "write_permission", nullable = false, columnDefinition = "varchar(10)")
    private MemberRole writePermission;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "boolean default false")
    private Boolean isDeleted;

    public Board() {}

    public Board(BoardName boardName, String urlPath, Long postCount, MemberRole readPermission, MemberRole writePermission, Boolean isDeleted) {
        this.boardName = boardName;
        this.urlPath = urlPath;
        this.postCount = postCount;
        this.readPermission = readPermission;
        this.writePermission = writePermission;
        this.isDeleted = isDeleted;
    }

    public Board(BoardCreateValues values) {
        this.boardName = values.getBoardName();
        this.urlPath = values.getUrlPath();
        this.postCount = 0L;
        this.readPermission = values.getReadPermission();
        this.writePermission = values.getWritePermission();
    }

    public void addPostCount() {
        this.postCount ++;
    }

    public void deductPostCount() {
        this.postCount --;
    }

    public void updateBoard(BoardUpdateValues boardUpdateValues) {
        this.urlPath = boardUpdateValues.getUrlPath();
        this.readPermission = boardUpdateValues.getReadPermission();
        this.writePermission = boardUpdateValues.getWritePermission();
    }

    private boolean onlyAdminAllowed(MemberRole permission, MemberRoles roles) {
        return permission.equals(MemberRole.ADMIN) && !roles.isAdmin();
    }

    private boolean onlyMemberAllowed(MemberRole permission, MemberRoles roles) {
        return permission.equals(MemberRole.MEMBER) && !roles.isMember();
    }

    public boolean isPublicReadPermission() {
        return this.readPermission.equals(MemberRole.PUBLIC);
    }

    public boolean memberHasWritePermissions(Member user) {
        if (onlyAdminAllowed(writePermission, user.getRoles())) {
            return false;
        }
        return !onlyMemberAllowed(writePermission, user.getRoles());
    }

    public boolean memberHasReadPermissions(MemberRoles roles) {
        if (onlyAdminAllowed(readPermission, roles)) {
            return false;
        }
        return !onlyMemberAllowed(readPermission, roles);
    }

    public void setIsDeleted() {
        this.isDeleted = true;
    }

}
