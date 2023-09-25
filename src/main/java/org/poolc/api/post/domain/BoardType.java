package org.poolc.api.post.domain;

import java.util.Arrays;

public enum BoardType {
    NOTICE(0L, "공지 게시판"),
    FREE(0L, "자유 게시판"),
    JOB(0L, "취업 게시판"),
    PROJECT(0L, "프로젝트 게시판"),
    CS(0L, "CS 게시판");

    private Long postCount;
    private final String boardName;

    private BoardType(Long postCount, String boardName) {
        this.postCount = postCount;
        this.boardName = boardName;
    }

    public Long getPostCount() {
        return postCount;
    }

    public static BoardType getBoardTypeByName(String name) {
        return Arrays.stream(BoardType.values())
                .filter(boardType -> boardType.boardName.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No board type found with given name."));
    }

    public static void addPostCount(BoardType boardType) {
        boardType.postCount++;
    }

    public static void removePostCount(BoardType boardType) {
        boardType.postCount --;
    }
}
