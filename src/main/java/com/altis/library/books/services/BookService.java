package com.altis.library.books.services;

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

    public BookService(BookRepository bookRepository, PublisherRepository publisherRepository) {
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
    }

    @Transactional
    public BookResponse create(BookCreateRequest request){
        if(bookRepository.existsByTitleIgnoreCaseAndAuthorContainingIgnoreCaseAndPublisherId(request.title(),  request.author(), request.publisherId())){
            throw new IllegalArgumentException("Esse livro já está cadastrado para essa editora!");
        }

        PublisherEntity publisher = publisherRepository.findById(request.publisherId())
                .orElseThrow(() -> new IllegalArgumentException("Editora não encontrada!"));

        BookEntity book = new BookEntity();
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setReleaseYear(request.releaseYear());
        book.setTotalQuantity(request.totalQuantity());
        book.setPublisher(publisher);

        BookEntity savedBook = bookRepository.save(book);
        return toResponse(savedBook);
    }

    @Transactional
    public BookResponse update(Long id, BookUpdateRequest request){
        BookEntity book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado"));
        if(request.totalQuantity() != null){
            if(request.totalQuantity() < book.getInUseQuantity()) {
                throw new IllegalArgumentException(
                        "O total de livros(" + request.totalQuantity() + ") não pode ser menor que a quantidad de livros emprestados (" + book.getInUseQuantity() + ")!"
                );
            }
            book.setTotalQuantity(request.totalQuantity());
        }

        if(request.title() != null && !request.title().isBlank()){
            book.setTitle(request.title());
        }

        if(request.author() != null && !request.author().isBlank()) {
            book.setAuthor(request.author());
        }

        if(request.releaseYear() != null){
            book.setReleaseYear(request.releaseYear());
        }

        if(request.publisherId() != null){
            PublisherEntity publisher = publisherRepository.findById(request.publisherId())
                    .orElseThrow(() -> new IllegalArgumentException("Editora não encontrada"));
            book.setPublisher(publisher);
        }

        BookEntity savedBook = bookRepository.save(book);
        return toResponse(savedBook);
    }

    @Transactional(readOnly = true)
    public Page<BookResponse> search(String term, Pageable pageable){
        return bookRepository
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(term, term, pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public BookResponse findById(Long id){
        return bookRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Esse livro não existe"));
    }

    @Transactional
    public void delete(Long id){
        BookEntity book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado"));
        if(book.getInUseQuantity() > 0){
            throw new IllegalStateException("Livro não pode ser excluido");
        }

        bookRepository.delete(book);

    }


    private BookResponse toResponse(BookEntity entity){
        return new BookResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getAuthor(),
                entity.getReleaseYear(),
                entity.getTotalQuantity(),
                entity.getInUseQuantity(),
                entity.getPublisher().getId(),
                entity.getPublisher().getName(),
                entity.getCreatedDate(),
                entity.getUpdatedDate()
        );
    }
}