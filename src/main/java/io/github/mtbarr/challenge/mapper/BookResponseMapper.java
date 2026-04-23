package io.github.mtbarr.challenge.mapper;

import org.mapstruct.Mapper;

import io.github.mtbarr.challenge.api.v1.response.BookResponse;
import io.github.mtbarr.challenge.domain.Book;

@Mapper(componentModel = "spring")
public interface BookResponseMapper {

    BookResponse toResponse(Book domain);
}
