package io.github.mtbarr.challenge.api.v1.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representação da resposta de um livro")
public record BookResponse(
    @Schema(description = "Identificador do livro", example = "1")
    Long id,

    @Schema(description = "Título do livro", example = "Clean Code")
    String title,

    @Schema(description = "Autor do livro", example = "Robert C. Martin")
    String author,

    @Schema(description = "Ano de publicação", example = "2008")
    Integer publicationYear
) {}