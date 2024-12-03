package org.poolc.api.book.service;

import org.poolc.api.book.dto.request.CreateBookRequest;
import org.poolc.api.book.dto.request.UpdateBookRequest;
import org.poolc.api.book.dto.response.BookResponse;
import org.poolc.api.member.domain.Member;
import org.springframework.data.domain.Page;


public interface BookService {

    Page<BookResponse> getAllBooks(int page);
    void createBook(Member member, CreateBookRequest request);
    void deleteBook(Member member, Long id) throws Exception;
    void updateBook(Member member, Long id, UpdateBookRequest request) throws Exception;
    void borrow(Member member, Long id) throws Exception;
    void returnBook(Member member, Long id) throws Exception;
    BookResponse getBook(Long id);

}
