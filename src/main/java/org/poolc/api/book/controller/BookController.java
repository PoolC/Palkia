package org.poolc.api.book.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.book.client.BookClient;
import org.poolc.api.book.dto.request.CreateBookRequest;
import org.poolc.api.book.dto.request.UpdateBookRequest;
import org.poolc.api.book.service.BookService;
import org.poolc.api.member.domain.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final BookClient bookClient;
    private final BookService bookService;

    @GetMapping("/search")
    public ResponseEntity<?> searchBooks(@RequestParam String query,
                                         @RequestParam(value = "page", defaultValue = "0") @Min(0) Integer page) {
        try {
            return new ResponseEntity<>(bookClient.searchBooks(query, page), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllBooks(@RequestParam(value = "page", defaultValue = "0") @Min(0) Integer page) {
        try {
            return new ResponseEntity<>(bookService.getAllBooks(page), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(bookService.getBook(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<?> addBook(@AuthenticationPrincipal Member member,
                                     @Valid @RequestBody CreateBookRequest request) {
        try {
            bookService.createBook(member, request);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@AuthenticationPrincipal Member member, @PathVariable Long id) {
        try {
            bookService.deleteBook(member, id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@AuthenticationPrincipal Member member, @PathVariable Long id,
                                        @Valid @RequestBody UpdateBookRequest request) {
        try {
            bookService.updateBook(member, id, request);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}/borrow")
    public ResponseEntity<?> borrowBook(@AuthenticationPrincipal Member member, @PathVariable Long id) {
        try {
            bookService.borrow(member, id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<?> returnBook(@AuthenticationPrincipal Member member, @PathVariable Long id) {
        try {
            bookService.returnBook(member, id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
