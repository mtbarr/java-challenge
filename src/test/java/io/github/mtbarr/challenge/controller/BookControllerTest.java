package io.github.mtbarr.challenge.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.mtbarr.challenge.api.v1.controller.BookControllerImpl;
import io.github.mtbarr.challenge.api.v1.request.CreateBookRequest;
import io.github.mtbarr.challenge.api.v1.request.UpdateBookRequest;
import io.github.mtbarr.challenge.api.v1.response.BookResponse;
import io.github.mtbarr.challenge.domain.Book;
import io.github.mtbarr.challenge.exception.ResourceNotFoundException;
import io.github.mtbarr.challenge.mapper.BookResponseMapper;
import io.github.mtbarr.challenge.service.BookService;
import org.springframework.data.domain.PageImpl;
import java.util.List;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CharacterEncodingFilter;

@WebMvcTest(BookControllerImpl.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private BookResponseMapper bookResponseMapper;

    @TestConfiguration
    static class EncodingConfig {
        @Bean
        public CharacterEncodingFilter characterEncodingFilter() {
            CharacterEncodingFilter filter = new CharacterEncodingFilter();
            filter.setEncoding("UTF-8");
            filter.setForceRequestEncoding(true);
            filter.setForceResponseEncoding(true);
            return filter;
        }
    }


    @Test
    void shouldReturn200WithBodyWhenGetBookById() throws Exception {
        final Long id = 1L;
        final Book book = new Book(id, "Título","Autor",2000);
        final BookResponse response = new BookResponse(id, "Título","Autor",2000);

        doReturn(book).when(bookService).getBookById(id);
        when(bookResponseMapper.toResponse(book)).thenReturn(response);

        MvcResult mvcResult = mockMvc.perform(get("/v1/books/{id}", id))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andReturn();

        final BookResponse actual = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), BookResponse.class);
        assertEquals(id, actual.id());
        assertEquals("Título", actual.title());
    }

    @Test
    void shouldReturn404WhenGetBookByIdNotFound() throws Exception {
        final Long id = 1L;
        when(bookService.getBookById(id)).thenThrow(new ResourceNotFoundException("not found"));

        mockMvc.perform(get("/v1/books/{id}", id))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn200WithPaginationWhenGetBooks() throws Exception {
        final Book b1 = new Book(1L, "Livro 1","Autor 1",2001);
        final Book b2 = new Book(2L, "Livro 2","Autor 2",2002);
        doReturn(new PageImpl<>(List.of(b1, b2))).when(bookService).getBooks(0, 10);
        when(bookResponseMapper.toResponse(b1)).thenReturn(new BookResponse(1L, "Livro 1","Autor 1",2001));
        when(bookResponseMapper.toResponse(b2)).thenReturn(new BookResponse(2L, "Livro 2","Autor 2",2002));

        mockMvc.perform(get("/v1/books").param("page", "0").param("size","10"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    void shouldReturn201WithBodyWhenPostCreateBook() throws Exception {
        final CreateBookRequest request = new CreateBookRequest("Título do Livro","Autor Exemplo",1999);
        final Book saved = new Book(1L, "Título do Livro","Autor Exemplo",1999);
        final BookResponse response = new BookResponse(1L, "Título do Livro","Autor Exemplo",1999);

        doReturn(saved).when(bookService).createBook(any(Book.class));
        when(bookResponseMapper.toResponse(saved)).thenReturn(response);

        MvcResult mvcResult = mockMvc.perform(post("/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andReturn();

        final BookResponse actual = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), BookResponse.class);
        assertEquals(1L, actual.id());
        assertEquals("Título do Livro", actual.title());
    }

    @Test
    void shouldReturn400WhenPostCreateBookInvalid() throws Exception {
        // title blank and publicationYear null -> validation error
        final String invalidJson = "{\"title\":\"\",\"author\":\"Autor\",\"publicationYear\":null}";

        mockMvc.perform(post("/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn200WithBodyWhenPutUpdateBook() throws Exception {
        final Long id = 1L;
        final UpdateBookRequest request = new UpdateBookRequest("Atualizado","AutorAtualizado",2010);
        final Book updated = new Book(id, "Atualizado","AutorAtualizado",2010);
        final BookResponse response = new BookResponse(id, "Atualizado","AutorAtualizado",2010);

        doReturn(updated).when(bookService).updateBook(eq(id), any(Book.class));
        when(bookResponseMapper.toResponse(updated)).thenReturn(response);

        MvcResult mvcResult = mockMvc.perform(put("/v1/books/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andReturn();

        final BookResponse actual = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), BookResponse.class);
        assertEquals("Atualizado", actual.title());
    }

    @Test
    void shouldReturn404WhenPutUpdateBookNotFound() throws Exception {
        final Long id = 1L;
        final UpdateBookRequest request = new UpdateBookRequest("Atualizado","AutorAtualizado",2010);
        doThrow(new ResourceNotFoundException("not found")).when(bookService).updateBook(eq(id), any(Book.class));

        mockMvc.perform(put("/v1/books/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn204WhenDeleteBook() throws Exception {
        final Long id = 1L;
        doNothing().when(bookService).deleteBook(id);

        mockMvc.perform(delete("/v1/books/{id}", id))
            .andExpect(status().isNoContent());

        verify(bookService).deleteBook(id);
    }

    @Test
    void shouldReturn404WhenDeleteBookNotFound() throws Exception {
        final Long id = 1L;
        doThrow(new ResourceNotFoundException("not found")).when(bookService).deleteBook(id);

        mockMvc.perform(delete("/v1/books/{id}", id))
            .andExpect(status().isNotFound());
    }
}







