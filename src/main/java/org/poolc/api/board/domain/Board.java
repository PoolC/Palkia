package org.poolc.api.board.domain;


import lombok.Getter;
import org.poolc.api.board.vo.BoardUpdateValues;
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

    @Column(name = "name", unique = true, nullable = false, columnDefinition = "varchar(40)")
    private String name;

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

    public Board() {}

    public Board(Long id, String name, String urlPath, Long postCount, MemberRole readPermission, MemberRole writePermission) {
        this.id = id;
        this.name = name;
        this.urlPath = urlPath;
        this.postCount = postCount;
        this.readPermission = readPermission;
        this.writePermission = writePermission;
    }

    public void addPostCount() {
        this.postCount ++;
    }

    public void decreasePostCount() {
        this.postCount --;
    }

    public void updateBoard(BoardUpdateValues boardUpdateValues) {
        this.name = boardUpdateValues.getName();
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

}
