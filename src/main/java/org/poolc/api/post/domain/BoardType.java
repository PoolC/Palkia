package org.poolc.api.post.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

public enum BoardType {
    NOTICE(0L, "notice"),
    FREE(0L, "free"),
    PROJECT(0L, "project"),
    EXTERNAL(0L, "external"),
    CAREER(0L, "career"),
    ETC(0L, "etc"),
    // Retained only to read existing rows. All public behavior treats it as ETC.
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
                .map(BoardType::canonicalize)
                .orElseThrow(() -> new IllegalArgumentException("No board type found with given name."));
    }

    public static BoardType canonicalize(BoardType boardType) {
        if (boardType == NOTICE || boardType == FREE || boardType == PROJECT
                || boardType == EXTERNAL || boardType == CAREER || boardType == ETC) {
            return boardType;
        }
        return ETC;
    }

    public static List<BoardType> storageTypesFor(BoardType boardType) {
        BoardType canonicalBoardType = canonicalize(boardType);
        return Arrays.stream(BoardType.values())
                .filter(candidate -> canonicalize(candidate) == canonicalBoardType)
                .collect(Collectors.toList());
    }

    public static void addPostCount(BoardType boardType) {
        canonicalize(boardType).postCount++;
    }

    public static void removePostCount(BoardType boardType) {
        canonicalize(boardType).postCount --;
    }
}
