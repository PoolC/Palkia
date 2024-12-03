package org.poolc.api.book.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookRequest {

    @Length(min = 1, max = 100)
    private String title;

    @Length(min = 1, max = 100)
    private String author;

    @Length(min = 1, max = 100)
    private String publisher;

    @Length(min = 1, max = 100)
    private String isbn;

    @Length(min = 1)
    private String description;

    @Length(min = 1, max = 100)
    private String pubdate;

    @Length(min = 1)
    private String image;

    @NotNull
    private Integer discount;

    @Length(min = 1)
    private String link;
}
