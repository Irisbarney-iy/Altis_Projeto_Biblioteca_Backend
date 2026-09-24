package com.altis.library.books.mappers;

import com.altis.library.books.models.dtos.BookCreateRequest;
import com.altis.library.books.models.dtos.BookResponse;
import com.altis.library.books.models.dtos.BookUpdateRequest;
import com.altis.library.books.models.entities.BookEntity;
import com.altis.library.publishers.models.entities.PublisherEntity;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public BookEntity toEntity(BookCreateRequest request, PublisherEntity publisher) {
        if (request == null) return null;

        BookEntity book = new BookEntity();
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setReleaseYear(request.releaseYear());
        book.setTotalQuantity(request.totalQuantity());
        book.setPublisher(publisher);

        return book;
    }

    public void updateEntityFromDto(BookUpdateRequest request, BookEntity book, PublisherEntity newPublisher) {
        if (request == null || book == null) return;
        if (request.totalQuantity() != null) {
            book.setTotalQuantity(request.totalQuantity());
        }
        if (request.title() != null && !request.title().isBlank()) {
            book.setTitle(request.title());
        }
        if (request.author() != null && !request.author().isBlank()) {
            book.setAuthor(request.author());
        }
        if (request.releaseYear() != null) {
            book.setReleaseYear(request.releaseYear());
        }
        if (newPublisher != null) {
            book.setPublisher(newPublisher);
        }
    }

    public BookResponse toResponse(BookEntity entity) {
        if (entity == null) return null;

        Long publisherId = entity.getPublisher() != null ? entity.getPublisher().getId() : null;
        String publisherName = entity.getPublisher() != null ? entity.getPublisher().getName() : null;

        return new BookResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getAuthor(),
                entity.getReleaseYear(),
                entity.getTotalQuantity(),
                entity.getInUseQuantity(),
                publisherId,
                publisherName
        );
    }
}