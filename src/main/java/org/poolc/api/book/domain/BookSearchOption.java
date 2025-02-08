package org.poolc.api.book.domain;

public enum BookSearchOption {
    TITLE("title"),
    AUTHOR("author"),
    TAG("tag");

    private final String value;

    BookSearchOption(String value) {
        this.value = value;
    }
}
