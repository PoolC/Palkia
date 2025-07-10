package org.poolc.api.book.domain;

public enum BookSortOption {

    CREATED_AT("createdAt"),
    TITLE("title"),
    RENT_TIME("rentTime");

    private final String value;

    BookSortOption(String value) {
        this.value = value;
    }

}
