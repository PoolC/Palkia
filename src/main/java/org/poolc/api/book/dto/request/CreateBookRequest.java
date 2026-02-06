package org.poolc.api.book.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookRequest {

    @NotBlank
    @Size(max = 30)
    private String title;

    @NotBlank
    @Size(max = 100)
    private String author;

    @Size(max = 100)
    private String publisher;

    @Size(max = 100)
    private String isbn;

    private String description;

    @Size(max = 100)
    private String pubdate;

    private String image;

    private Integer discount;

    private String link;

    @Size(max = 100)
    private String donor;

    @Size(max = 10)
    private List<String> tags;
}
