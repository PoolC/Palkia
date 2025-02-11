package org.poolc.api.book.repository;

import org.poolc.api.book.domain.Book;
import org.poolc.api.book.domain.BookSortOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByTitleAndAuthor(String title, String author);

    Optional<Book> findBookById(Long id);
    Page<Book> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Book> findAllByOrderByTitleAsc(Pageable pageable);

    @Query("SELECT b FROM Book b WHERE b.title LIKE '%:keyword%' " +
            "ORDER BY " +
            "CASE WHEN :sortOption = 'TITLE' THEN b.title END ASC, " +
            "CASE WHEN :sortOption = 'CREATED_AT' THEN b.createdAt END DESC, " +
            "CASE WHEN :sortOption = 'RENT_TIME' THEN b.rentDate END DESC, " +
            "CASE WHEN :sortOption = 'RENT_TIME' THEN b.title END ASC")
    Page<Book> findAllByTitleContaining(String keyword,String sortOption, Pageable pageable);

    @Query("SELECT b FROM Book b WHERE b.author LIKE '%:keyword%' " +
            "ORDER BY " +
            "CASE WHEN :sortOption = 'TITLE' THEN b.title END ASC, " +
            "CASE WHEN :sortOption = 'CREATED_AT' THEN b.createdAt END DESC, " +
            "CASE WHEN :sortOption = 'RENT_TIME' THEN b.rentDate END DESC, " +
            "CASE WHEN :sortOption = 'RENT_TIME' THEN b.title END ASC")
    Page<Book> findAllByAuthorContaining(String keyword, String sortOption, Pageable pageable);

    @Query("SELECT b FROM Book b WHERE :keyword MEMBER OF b.tags " +
            "ORDER BY " +
            "CASE WHEN :sortOption = 'TITLE' THEN b.title END ASC, " +
            "CASE WHEN :sortOption = 'CREATED_AT' THEN b.createdAt END DESC, " +
            "CASE WHEN :sortOption = 'RENT_TIME' THEN b.rentDate END DESC, " +
            "CASE WHEN :sortOption = 'RENT_TIME' THEN b.title END ASC")
    Page<Book> findAllByTagsContaining(String keyword,String sortOption, Pageable pageable);

    @Query("SELECT b FROM Book b " +
            "ORDER BY CASE WHEN b.rentDate IS NULL THEN 0 ELSE 1 END, " +
            "b.rentDate DESC, " +
            "b.title ASC")
    Page<Book> findAllByOrderByRentDateDescTitleAsc(Pageable pageable);

}