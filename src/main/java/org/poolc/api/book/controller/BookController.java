package org.poolc.api.book.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.book.client.BookClient;
import org.poolc.api.book.domain.BookSearchOption;
import org.poolc.api.book.domain.BookSortOption;
import org.poolc.api.book.dto.request.CreateBookRequest;
import org.poolc.api.book.dto.request.UpdateBookRequest;
import org.poolc.api.book.dto.response.BookApiResponse;
import org.poolc.api.book.dto.response.BookResponse;
import org.poolc.api.book.service.BookService;
import org.poolc.api.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final BookClient bookClient;
    private final BookService bookService;

    @GetMapping("/naver/search")
    public ResponseEntity<List<BookApiResponse>> searchBooksFromAPI(@RequestParam String query,
                                                             @RequestParam(value = "page", defaultValue = "0") @Min(0) Integer page) {
        try {
            return new ResponseEntity<>(bookClient.searchBooks(query, page), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<BookResponse>> searchBooks(
            @RequestParam(value = "page", defaultValue = "0") @Min(0) Integer page,
            @RequestParam(value = "search", required = true)BookSearchOption searchOption,
            @RequestParam(value = "keyword", required = true) String keyword,
            @RequestParam(value = "sort", required = false) BookSortOption sortOption) {
        try {
            return new ResponseEntity<>(bookService.searchBooks(page,searchOption,keyword,sortOption), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Page<BookResponse>> getAllBooks(
            @RequestParam(value = "page", defaultValue = "0") @Min(0) Integer page,
            @RequestParam(value = "sort", required = false) BookSortOption sortOption) {
        try {
            return new ResponseEntity<>(bookService.getAllBooks(page, sortOption), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBook(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(bookService.getBook(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<Void> addBook(@AuthenticationPrincipal Member member,
                                     @Valid @RequestBody CreateBookRequest request) {
        try {
            bookService.createBook(member, request);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@AuthenticationPrincipal Member member, @PathVariable Long id) {
        try {
            bookService.deleteBook(member, id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBook(@AuthenticationPrincipal Member member, @PathVariable Long id,
                                        @Valid @RequestBody UpdateBookRequest request) {
        try {
            bookService.updateBook(member, id, request);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}/borrow")
    public ResponseEntity<Void> borrowBook(@AuthenticationPrincipal Member member, @PathVariable Long id) {
        try {
            bookService.rent(member, id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<Void> returnBook(@AuthenticationPrincipal Member member, @PathVariable Long id) {
        try {
            bookService.returnBook(member, id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
