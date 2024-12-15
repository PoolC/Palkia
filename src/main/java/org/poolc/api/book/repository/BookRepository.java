package org.poolc.api.book.repository;

import org.poolc.api.book.domain.Book;
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

    @Query("SELECT b FROM Book b " +
            "ORDER BY CASE WHEN b.rentDate IS NULL THEN 0 ELSE 1 END, " +
            "b.rentDate DESC, " +
            "b.title ASC")
    Page<Book> findAllByOrderByRentDateDescTitleAsc(Pageable pageable);

}