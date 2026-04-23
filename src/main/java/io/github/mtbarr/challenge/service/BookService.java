package io.github.mtbarr.challenge.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import io.github.mtbarr.challenge.data.entity.BookEntity;
import io.github.mtbarr.challenge.data.repository.BookRepository;
import io.github.mtbarr.challenge.domain.Book;
import io.github.mtbarr.challenge.exception.ResourceNotFoundException;
import io.github.mtbarr.challenge.mapper.BookMapper;

@Service
public class BookService {

    private final @NonNull BookRepository bookRepository;
    private final @NonNull BookMapper bookMapper;

    public BookService(@NonNull BookRepository bookRepository, @NonNull BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    public @NonNull Book getBookById(@NonNull Long id) {
        return bookRepository.findById(id)
            .map(bookMapper::toDomain)
            .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    public @NonNull Book createBook(@NonNull Book book) {
        final BookEntity savedEntity = bookRepository.save(bookMapper.toEntity(book));
        return bookMapper.toDomain(savedEntity);
    }

    public @NonNull Book updateBook(@NonNull Long id, @NonNull Book book) {
        final BookEntity entity = bookRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        entity.setTitle(book.title());
        entity.setAuthor(book.author());
        entity.setPublicationYear(book.publicationYear());

        return bookMapper.toDomain(bookRepository.save(entity));
    }

    public void deleteBook(@NonNull Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    public @NonNull Page<Book> getBooks(int page, int size) {
        return bookRepository.findAll(PageRequest.of(page, size))
            .map(bookMapper::toDomain);
    }
}