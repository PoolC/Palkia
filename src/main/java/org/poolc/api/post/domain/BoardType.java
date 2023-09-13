package org.poolc.api.post.domain;

public enum BoardType {
    NOTICE(0L),
    FREE(0L),
    JOB(0L),
    PROJECT(0L),
    CS(0L);

    private Long postCount;

    private BoardType(Long postCount) {
        this.postCount = postCount;
    }

    public Long getPostCount() {
        return postCount;
    }

    public static BoardType getBoardTypeByName(String name) {
        for (BoardType boardType : BoardType.values()) {
            if (boardType.toString().equals(name)) {
                return boardType;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 게시판입니다.");
    }

    public static void addPostCount(BoardType boardType) {
        boardType.postCount++;
    }

    public static void removePostCount(BoardType boardType) {
        boardType.postCount --;
    }
}
