package io.github.mtbarr.challenge.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import io.github.mtbarr.challenge.data.entity.BookEntity;

public interface BookRepository extends JpaRepository<BookEntity, Long> {

    @NonNull
    Page<BookEntity> findAll(@NonNull Pageable pageable);

    @NonNull
    default Page<BookEntity> findAll(int page, int size) {
        return findAll(PageRequest.of(page, size));
    }
}
