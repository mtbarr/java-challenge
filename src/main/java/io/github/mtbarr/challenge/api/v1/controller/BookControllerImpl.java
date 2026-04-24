package io.github.mtbarr.challenge.api.v1.controller;

import io.github.mtbarr.challenge.api.v1.request.CreateBookRequest;
import io.github.mtbarr.challenge.api.v1.request.UpdateBookRequest;
import io.github.mtbarr.challenge.api.v1.response.BookResponse;
import io.github.mtbarr.challenge.domain.Book;
import io.github.mtbarr.challenge.mapper.BookResponseMapper;
import io.github.mtbarr.challenge.service.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/books")
@Validated
public class BookControllerImpl implements BookControllerApi {

    private final BookService bookService;
    private final BookResponseMapper bookResponseMapper;

    public BookControllerImpl(
            BookService bookService,
            BookResponseMapper bookResponseMapper
    ) {
        this.bookService = bookService;
        this.bookResponseMapper = bookResponseMapper;
    }

    @PostMapping
    public ResponseEntity<BookResponse> createBook(
            @RequestBody @Valid CreateBookRequest request
    ) {
        final Book book = new Book(
                null,
                request.title(),
                request.author(),
                request.publicationYear());

        final Book saved = bookService.createBook(book);
        return ResponseEntity.status(201)
                .body(bookResponseMapper.toResponse(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(
            @PathVariable Long id
    ) {
        final Book book = bookService.getBookById(id);
        final BookResponse response = bookResponseMapper.toResponse(book);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<BookResponse>> getBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size
    ) {

        final Page<BookResponse> responsePage = bookService.getBooks(page, size)
                .map(bookResponseMapper::toResponse);
        return ResponseEntity.ok(responsePage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(
            @PathVariable Long id,
            @RequestBody @Valid UpdateBookRequest request
    ) {

        final Book bookToUpdate = new Book(
                id,
                request.title(),
                request.author(),
                request.publicationYear());

        final Book updated = bookService.updateBook(id, bookToUpdate);
        return ResponseEntity.ok(bookResponseMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
