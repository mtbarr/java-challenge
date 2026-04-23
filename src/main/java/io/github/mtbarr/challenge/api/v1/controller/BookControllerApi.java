package io.github.mtbarr.challenge.api.v1.controller;

import io.github.mtbarr.challenge.api.v1.request.CreateBookRequest;
import io.github.mtbarr.challenge.api.v1.request.UpdateBookRequest;
import io.github.mtbarr.challenge.api.v1.response.BookResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

@Tag(name = "Livros", description = "Operações relacionadas a livros")
public interface BookControllerApi {

    @Operation(summary = "Criar um novo livro", description = "Cria um livro e retorna os dados salvos.")
    @ApiResponse(responseCode = "201", description = "Livro criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookResponse.class)))
    ResponseEntity<BookResponse> createBook(@Valid CreateBookRequest request);

    @Operation(summary = "Buscar livro por id", description = "Retorna os dados do livro solicitado.")
    @ApiResponse(responseCode = "200", description = "Livro encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookResponse.class)))
    @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    ResponseEntity<BookResponse> getBookById(Long id);

    @Operation(summary = "Listar livros paginados", description = "Retorna uma página de livros.")
    @ApiResponse(responseCode = "200", description = "Página de livros retornada")
    ResponseEntity<Page<BookResponse>> getBooks(int page, int size);

    @Operation(summary = "Atualizar livro", description = "Atualiza os dados de um livro existente.")
    @ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookResponse.class)))
    @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    ResponseEntity<BookResponse> updateBook(Long id, @Valid UpdateBookRequest request);

    @Operation(summary = "Excluir livro", description = "Remove um livro pelo id.")
    @ApiResponse(responseCode = "204", description = "Livro excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    ResponseEntity<Void> deleteBook(Long id);
}

