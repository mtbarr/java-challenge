package io.github.mtbarr.challenge.mapper;

import org.mapstruct.Mapper;

import io.github.mtbarr.challenge.data.entity.BookEntity;
import io.github.mtbarr.challenge.domain.Book;

@Mapper(componentModel = "spring")
public interface BookMapper {

    Book toDomain(BookEntity entity);

    BookEntity toEntity(Book domain);
}
