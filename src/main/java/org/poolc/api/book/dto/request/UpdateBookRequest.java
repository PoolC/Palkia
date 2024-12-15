package org.poolc.api.book.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBookRequest {

    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private String description;
    private String pubdate;
    private String image;
    private Integer discount;
    private String link;
    private String donor;

}
