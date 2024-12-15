package org.poolc.api.book.domain;

public enum BookSort {

    CREATED_AT("createdAt"),
    TITLE("title"),
    RENT_TIME("rentTime");

    private final String value;

    BookSort(String value) {
        this.value = value;
    }

}
