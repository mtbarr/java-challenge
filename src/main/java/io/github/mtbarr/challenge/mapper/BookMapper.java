package io.github.mtbarr.challenge.mapper;

import org.mapstruct.Mapper;
import org.springframework.lang.NonNull;

import io.github.mtbarr.challenge.data.entity.BookEntity;
import io.github.mtbarr.challenge.domain.Book;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @NonNull
    Book toDomain(@NonNull BookEntity entity);

    @NonNull
    BookEntity toEntity(@NonNull Book domain);
}
