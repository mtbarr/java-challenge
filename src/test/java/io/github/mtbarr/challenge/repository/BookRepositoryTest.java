package io.github.mtbarr.challenge.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import io.github.mtbarr.challenge.data.entity.BookEntity;
import io.github.mtbarr.challenge.data.repository.BookRepository;

@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void shouldPersistAndFindByIdWhenSave() {
        final BookEntity entity = new BookEntity("Título Repositório","Autor Repositório",1990);
        final BookEntity saved = bookRepository.save(entity);

        assertNotNull(saved.getId());

        final var found = bookRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Título Repositório", found.get().getTitle());
    }

    @Test
    void shouldDeleteAndNotExistAfterDelete() {
        final BookEntity entity = new BookEntity("Para Deletar","Autor",1980);
        final BookEntity saved = bookRepository.save(entity);
        final Long id = saved.getId();

        bookRepository.deleteById(id);

        assertFalse(bookRepository.existsById(id));
    }

    @Test
    void shouldReturnPageWhenFindAllWithPageRequest() {
        bookRepository.save(new BookEntity("Livro 1","Autor 1",2001));
        bookRepository.save(new BookEntity("Livro 2","Autor 2",2002));

        final Page<BookEntity> page = bookRepository.findAll(PageRequest.of(0, 10));

        assertTrue(page.getTotalElements() >= 2);
        assertFalse(page.getContent().isEmpty());
    }
}

