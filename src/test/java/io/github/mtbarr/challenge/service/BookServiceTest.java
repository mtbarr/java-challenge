package io.github.mtbarr.challenge.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import io.github.mtbarr.challenge.data.entity.BookEntity;
import io.github.mtbarr.challenge.data.repository.BookRepository;
import io.github.mtbarr.challenge.domain.Book;
import io.github.mtbarr.challenge.exception.ResourceNotFoundException;
import io.github.mtbarr.challenge.mapper.BookMapper;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    @Test
    void shouldReturnBookWhenGetBookByIdFound() {
        final Long id = 1L;
        final BookEntity entity = new BookEntity("Título","Autor",2000);
        // set id via reflection-like approach: repository will return this entity and mapper controls domain
        when(bookRepository.findById(id)).thenReturn(Optional.of(entity));
        final Book expected = new Book(id, "Título", "Autor", 2000);
        when(bookMapper.toDomain(entity)).thenReturn(expected);

        final Book actual = bookService.getBookById(id);

        assertEquals(expected, actual);
        verify(bookRepository).findById(id);
        verify(bookMapper).toDomain(entity);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenGetBookByIdNotFound() {
        final Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.getBookById(id));
        verify(bookRepository).findById(id);
        verifyNoInteractions(bookMapper);
    }

    @Test
    void shouldPersistAndReturnCreatedBookWhenCreateBook() {
        final Book toCreate = new Book(null, "Novo Título", "Novo Autor", 1999);
        final BookEntity toEntity = new BookEntity("Novo Título", "Novo Autor", 1999);
        final BookEntity savedEntity = new BookEntity("Novo Título", "Novo Autor", 1999);
        // mimic saved id by using a subclass or replacing behavior via mapper
        when(bookMapper.toEntity(toCreate)).thenReturn(toEntity);
        when(bookRepository.save(toEntity)).thenReturn(savedEntity);
        final Book expected = new Book(1L, "Novo Título", "Novo Autor", 1999);
        when(bookMapper.toDomain(savedEntity)).thenReturn(expected);

        final Book actual = bookService.createBook(toCreate);

        assertEquals(expected, actual);
        verify(bookMapper).toEntity(toCreate);
        verify(bookRepository).save(toEntity);
        verify(bookMapper).toDomain(savedEntity);
    }

    @Test
    void shouldUpdateFieldsWhenUpdateBookExists() {
        final Long id = 1L;
        final BookEntity existing = new BookEntity("Antigo","AutorAntigo",1900);
        when(bookRepository.findById(id)).thenReturn(Optional.of(existing));

        final Book updatedDomain = new Book(id, "Novo Título", "Novo Autor", 2020);

        when(bookRepository.save(any(BookEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(bookMapper.toDomain(any(BookEntity.class))).thenReturn(updatedDomain);

        final Book result = bookService.updateBook(id, updatedDomain);

        assertEquals(updatedDomain, result);

        ArgumentCaptor<BookEntity> captor = ArgumentCaptor.forClass(BookEntity.class);
        verify(bookRepository).save(captor.capture());
        BookEntity saved = captor.getValue();
        assertEquals("Novo Título", saved.getTitle());
        assertEquals("Novo Autor", saved.getAuthor());
        assertEquals(Integer.valueOf(2020), saved.getPublicationYear());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUpdateBookNotFound() {
        final Long id = 1L;
        final Book updated = new Book(id, "T","A",2001);
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.updateBook(id, updated));
        verify(bookRepository).findById(id);
    }

    @Test
    void shouldCallDeleteByIdWhenBookExists() {
        final Long id = 1L;
        when(bookRepository.existsById(id)).thenReturn(true);

        bookService.deleteBook(id);

        verify(bookRepository).existsById(id);
        verify(bookRepository).deleteById(id);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenDeleteBookNotFound() {
        final Long id = 1L;
        when(bookRepository.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> bookService.deleteBook(id));
        verify(bookRepository).existsById(id);
        verify(bookRepository, never()).deleteById(any());
    }

    @Test
    void shouldReturnPageOfBooksWhenGetBooks() {
        final int page = 0, size = 10;
        final BookEntity e1 = new BookEntity("T1","A1",2001);
        final BookEntity e2 = new BookEntity("T2","A2",2002);
        final List<BookEntity> entities = List.of(e1, e2);
        final Page<BookEntity> entityPage = new PageImpl<>(entities);

        when(bookRepository.findAll(any(Pageable.class))).thenReturn(entityPage);
        when(bookMapper.toDomain(e1)).thenReturn(new Book(1L, "T1","A1",2001));
        when(bookMapper.toDomain(e2)).thenReturn(new Book(2L, "T2","A2",2002));

        final Page<Book> result = bookService.getBooks(page, size);

        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        verify(bookRepository).findAll(any(Pageable.class));
        verify(bookMapper, times(2)).toDomain(any(BookEntity.class));
    }
}

