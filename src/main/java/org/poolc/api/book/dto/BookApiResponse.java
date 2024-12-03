package org.poolc.api.book.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookApiResponse {

    @JacksonXmlProperty(localName = "title")
    private String title;

    @JacksonXmlProperty(localName = "link")
    private String link;

    @JacksonXmlProperty(localName = "image")
    private String image;

    @JacksonXmlProperty(localName = "author")
    private String author;

    @JacksonXmlProperty(localName = "discount")
    private Integer discount;

    @JacksonXmlProperty(localName = "publisher")
    private String publisher;

    @JacksonXmlProperty(localName = "isbn")
    private String isbn;

    @JacksonXmlProperty(localName = "description")
    private String description;

    @JacksonXmlProperty(localName = "pubdate")
    private String pubdate;

}
