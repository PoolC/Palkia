package org.poolc.api.book.repository;

import org.poolc.api.book.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByTitleAndAuthor(String title, String author);

    Optional<Book> findBookById(Long id);
    Page<Book> findAllByOrderByCreatedAtDesc(Pageable pageable);

}