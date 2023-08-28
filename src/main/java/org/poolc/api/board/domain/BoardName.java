package org.poolc.api.board.domain;

public enum BoardName {
    MAIN("자유게시판"),
    JOB("취업합시다"),
    CS("CS 게시판"),
    PROJECT("사람 구해요~");

    private final String description;

    private BoardName(String description) { this.description = description; }

    public String getDescription() { return this.description; }
}
