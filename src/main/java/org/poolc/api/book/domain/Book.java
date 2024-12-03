package org.poolc.api.book.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.poolc.api.common.domain.TimestampEntity;
import org.poolc.api.member.domain.Member;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@SequenceGenerator(
        name = "BOOK_SEQ_GENERATOR",
        sequenceName = "BOOK_SEQ"
)
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
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

    @Column(name = "link")
    private String link;

    @Column(name = "image_url")
    private String imageURL;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "description")
    private String description;

    @Column(name = "discount")
    private Integer discount;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "published_date")
    private String publishedData;

    @Column(name = "borrow_date")
    private LocalDate borrowDate;

    @Column(name = "status", columnDefinition = "varchar(64) default 'AVAILABLE'")
    @Enumerated(EnumType.STRING)
    private BookStatus status = BookStatus.AVAILABLE;

    protected Book() {
    }

    public void borrowBook(Member member) {
        this.status = BookStatus.UNAVAILABLE;
        this.borrowDate = LocalDate.now();
        this.borrower = member;
    }

    public void returnBook() {
        this.status = BookStatus.AVAILABLE;
        this.borrowDate = null;
        this.borrower = null;
    }

}
