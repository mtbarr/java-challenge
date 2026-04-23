package io.github.mtbarr.challenge.domain;

public record Book(
    Long id,
    String title,
    String author,
    Integer publicationYear
) {}