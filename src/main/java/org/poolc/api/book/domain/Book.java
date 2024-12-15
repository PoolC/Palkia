package org.poolc.api.book.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.poolc.api.book.dto.request.UpdateBookRequest;
import org.poolc.api.common.domain.TimestampEntity;
import org.poolc.api.member.domain.Member;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Getter
@SequenceGenerator(
        name = "BOOK_SEQ_GENERATOR",
        sequenceName = "BOOK_SEQ"
)
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class Book extends TimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOOK_SEQ_GENERATOR")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "borrower", referencedColumnName = "UUID")
    private Member borrower = null;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "link", columnDefinition = "VARCHAR(600)")
    private String link;

    @Column(name = "image_url", columnDefinition = "VARCHAR(600)")
    private String imageURL;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "discount")
    private Integer discount;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "published_date")
    private String publishedDate;

    @Column(name = "rent_date")
    private LocalDate rentDate;

    @Column(name = "status", columnDefinition = "varchar(64) default 'AVAILABLE'")
    @Enumerated(EnumType.STRING)
    private BookStatus status = BookStatus.AVAILABLE;

    protected Book() {
    }

    public void rentBook(Member member) {
        this.status = BookStatus.UNAVAILABLE;
        this.rentDate = LocalDate.now();
        this.renter = member;
    }

    public void returnBook() {
        this.status = BookStatus.AVAILABLE;
        this.rentDate = null;
        this.renter = null;
    }

    public void update(UpdateBookRequest request) {
        if (request.getTitle() != null) this.title = request.getTitle();
        if (request.getLink() != null) this.link = request.getLink();
        if (request.getImage() != null) this.imageURL = request.getImage();
        if (request.getAuthor() != null) this.author = request.getAuthor();
        if (request.getDescription() != null) this.description = request.getDescription();
        if (request.getDiscount() != null) this.discount = request.getDiscount();
        if (request.getIsbn() != null) this.isbn = request.getIsbn();
        if (request.getPublisher() != null) this.publisher = request.getPublisher();
        if (request.getPubdate() != null) this.publishedDate = request.getPubdate();
    }

}
