package io.github.mtbarr.challenge.api.v1.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(name = "CreateBookRequest", description = "Requisição para criar um novo livro")
public record CreateBookRequest(
    @NotBlank(message = "Título é obrigatório")
    @Schema(description = "Título do livro", example = "O Senhor dos Anéis")
    String title,

    @NotBlank(message = "Autor é obrigatório")
    @Schema(description = "Nome do autor", example = "J.R.R. Tolkien")
    String author,

    @NotNull(message = "Ano de publicação é obrigatório")
    @Positive(message = "Ano de publicação deve ser positivo")
    @Schema(description = "Ano de publicação", example = "1954")
    Integer publicationYear
) {}