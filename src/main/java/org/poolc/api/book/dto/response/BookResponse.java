package org.poolc.api.book.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.poolc.api.book.domain.Book;
import org.poolc.api.book.domain.BookStatus;

import java.time.LocalDate;
import java.util.List;

import org.poolc.api.member.dto.MemberResponse;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {

    private Long id;
    private String title;
    private String link;
    private String imageURL;
    private String author;
    private String description;
    private Integer discount;
    private String isbn;
    private String publisher;
    private String publishedDate;
    private LocalDate borrowDate;
    private BookStatus status;

    private MemberResponse borrower;
    private String donor;
    private List<String> tags;

    public static BookResponse of(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .link(book.getLink())
                .imageURL(book.getImageURL())
                .author(book.getAuthor())
                .description(book.getDescription())
                .discount(book.getDiscount())
                .isbn(book.getIsbn())
                .publisher(book.getPublisher())
                .publishedDate(book.getPublishedDate())
                .borrowDate(book.getRentDate())
                .status(book.getStatus())
                .donor(book.getDonor())
                .borrower(book.getRenter() == null ? null : MemberResponse.of(book.getRenter()))
                .tags(book.getTags())
                .build();
    }

}
