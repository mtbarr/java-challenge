package io.github.mtbarr.challenge.api.v1.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ApiErrorResponse(
  @Schema(description = "Horario da requisição (em milisegundos)", example = "1629780000000")
  long timeStamp,

  @Schema(description = "Status da requisição", example = "400")
  int status,

  @Schema(description = "Mensagem de erro", example = "Contéudo inválido")
  String message,

  @Schema(description = "Caminho da requisição", example = "/api/v1/subject")
  String path,

  @Schema(description = "Método da requisição", example = "POST")
  String method
) {

  public ApiErrorResponse(
    @Schema(description = "Status da requisição", example = "400") int status,
    @Schema(description = "Mensagem de erro", example = "Contéudo inválido") String message,
    @Schema(description = "Caminho da requisição", example = "/api/v1/subject") String path,
    @Schema(description = "Método da requisição", example = "POST") String method
  ) {

    this(System.currentTimeMillis(), status, message, path, method);
  }
}