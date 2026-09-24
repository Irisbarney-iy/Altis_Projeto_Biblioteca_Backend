package com.altis.library.books.services;

import com.altis.library.books.mappers.BookMapper;
import com.altis.library.books.models.dtos.BookCreateRequest;
import com.altis.library.books.models.dtos.BookResponse;
import com.altis.library.books.models.dtos.BookUpdateRequest;
import com.altis.library.books.models.entities.BookEntity;
import com.altis.library.books.repositories.BookRepository;
import com.altis.library.publishers.models.entities.PublisherEntity;
import com.altis.library.publishers.repositories.PublisherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository, PublisherRepository publisherRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
        this.bookMapper = bookMapper;
    }

    @Transactional
    public BookResponse create(BookCreateRequest request) {
        if (bookRepository.existsByTitleIgnoreCaseAndAuthorContainingIgnoreCaseAndPublisherId(
                request.title(), request.author(), request.publisherId())) {
            throw new IllegalArgumentException("Esse livro já está cadastrado para essa editora!");
        }

        PublisherEntity publisher = publisherRepository.findById(request.publisherId())
                .orElseThrow(() -> new IllegalArgumentException("Editora não encontrada!"));

        BookEntity book = bookMapper.toEntity(request, publisher);
        BookEntity savedBook = bookRepository.save(book);

        return bookMapper.toResponse(savedBook);
    }

    @Transactional
    public BookResponse update(Long id, BookUpdateRequest request) {
        BookEntity book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado"));

        if (request.totalQuantity() != null) {
            if (request.totalQuantity() < book.getInUseQuantity()) {
                throw new IllegalArgumentException(
                        "O total de livros(" + request.totalQuantity() + ") não pode ser menor que a quantidade de livros emprestados (" + book.getInUseQuantity() + ")!"
                );
            }
        }

        PublisherEntity newPublisher = null;
        if (request.publisherId() != null) {
            newPublisher = publisherRepository.findById(request.publisherId())
                    .orElseThrow(() -> new IllegalArgumentException("Editora não encontrada"));
        }

        bookMapper.updateEntityFromDto(request, book, newPublisher);
        BookEntity savedBook = bookRepository.save(book);

        return bookMapper.toResponse(savedBook);
    }

    @Transactional(readOnly = true)
    public Page<BookResponse> search(String term, Pageable pageable) {
        return bookRepository
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(term, term, pageable)
                .map(bookMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public BookResponse findById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Esse livro não existe"));
    }

    @Transactional
    public void delete(Long id) {
        BookEntity book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado"));

        if (book.getInUseQuantity() > 0) {
            throw new IllegalStateException("Livro não pode ser excluído pois possui exemplares emprestados!");
        }

        bookRepository.delete(book);
    }
}