package org.poolc.api.book.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.book.client.BookClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final BookClient bookClient;

    @GetMapping("/search")
    public ResponseEntity<?> searchBooks(@RequestParam String query, @RequestParam int page) {
        try {
            return new ResponseEntity<>(bookClient.searchBooks(query, page), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
