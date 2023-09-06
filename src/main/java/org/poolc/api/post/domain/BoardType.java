package org.poolc.api.post.domain;

public enum BoardType {
    NOTICE("공지게시판"),
    FREE("자유게시판"),
    JOB("취업게시판"),
    PROJECT("사람구해요~"),
    CS("CS게시판");

    private String name;

    private BoardType(String description) {
        this.name = description;
    }

    public String getDescription() {
        return name;
    }

    public static BoardType getBoardTypeByName(String name) {
        for (BoardType boardType : BoardType.values()) {
            if (boardType.name.equals(name)) {
                return boardType;
            }
        }
        return null;
    }
}
