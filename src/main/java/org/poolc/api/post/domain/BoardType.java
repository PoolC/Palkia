package org.poolc.api.post.domain;

import java.util.Arrays;
import lombok.Getter;

public enum BoardType {
    NOTICE(0L, "notice"),
    FREE(0L, "free"),
    PROJECT(0L, "project"),
    EXTERNAL(0L, "external"),
    CAREER(0L, "career"),
    STAFF(0L, "staff");

    @Getter
    private Long postCount;
    private final String boardName;

    private BoardType(Long postCount, String boardName) {
        this.postCount = postCount;
        this.boardName = boardName;
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
