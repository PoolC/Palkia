package org.poolc.api.book.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.book.domain.Book;
import org.poolc.api.book.domain.BookStatus;
import org.poolc.api.book.dto.request.CreateBookRequest;
import org.poolc.api.book.dto.request.UpdateBookRequest;
import org.poolc.api.book.dto.response.BookResponse;
import org.poolc.api.book.repository.BookRepository;
import org.poolc.api.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private static final int PAGE_SIZE = 10;

    @Override
    public Page<BookResponse> getAllBooks(int page) {
        return bookRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, PAGE_SIZE))
                .map(BookResponse::of);
    }

    @Override
    @Transactional
    public void createBook(Member member, CreateBookRequest request) {
        checkAdmin(member);
        Book book = Book.builder()
                .title(request.getTitle())
                .link(request.getLink())
                .author(request.getAuthor())
                .discount(request.getDiscount())
                .imageURL(request.getImage())
                .publishedDate(request.getPubdate())
                .publisher(request.getPublisher())
                .isbn(request.getIsbn())
                .description(request.getDescription())
                .status(BookStatus.AVAILABLE)
                .borrowDate(null)
                .build();
        bookRepository.save(book);
    }

    @Override
    @Transactional
    public void deleteBook(Member member, Long id) throws Exception {
        checkAdmin(member);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new Exception("책을 찾을 없습니다. id: " + id));
        bookRepository.delete(book);
    }

    @Override
    @Transactional
    public void updateBook(Member member, Long id, UpdateBookRequest request) throws Exception {
        checkAdmin(member);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new Exception("책을 찾을 없습니다. id: " + id));
        book.update(request);
    }

    @Override
    @Transactional
    public void rent(Member member, Long id) throws Exception {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new Exception("책을 찾을 없습니다. id: " + id));
        if (book.getStatus() == BookStatus.UNAVAILABLE) {
            throw new Exception("대여 중인 책입니다. id: " + id);
        }
        book.rentBook(member);
    }

    @Override
    @Transactional
    public void returnBook(Member member, Long id) throws Exception {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new Exception("책을 찾을 없습니다. id: " + id));
        if (!book.getRenter().equals(member)) {
            throw new Exception("대여한 사람만 반납할 수 있습니다. id: " + id);
        }
        if (book.getStatus() == BookStatus.AVAILABLE) {
            throw new Exception("반납할 수 없는 책입니다. id: " + id);
        }
        book.returnBook();
    }

    @Override
    public BookResponse getBook(Long id) {
        return bookRepository.findBookById(id)
                .map(BookResponse::of)
                .orElseThrow(() -> new IllegalArgumentException("책을 찾을 없습니다. id: " + id));
    }

    private void checkAdmin(Member member) {
        if (!member.isAdmin()) {
            throw new IllegalArgumentException("관리자만 접근할 수 있습니다.");
        }
    }

}
