package org.poolc.api.book.repository;

import org.poolc.api.book.domain.Book;
import org.poolc.api.book.domain.BookSortOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    boolean existsByTitleAndAuthor(String title, String author);

    Optional<Book> findBookById(Long id);
    Page<Book> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Book> findAllByOrderByTitleAsc(Pageable pageable);

    Page<Book> findAll(Specification<Book> spec, Pageable pageable);

    @Query("SELECT b FROM Book b " +
            "ORDER BY CASE WHEN b.rentDate IS NULL THEN 0 ELSE 1 END, " +
            "b.rentDate DESC, " +
            "b.title ASC")
    Page<Book> findAllByOrderByRentDateDescTitleAsc(Pageable pageable);

}