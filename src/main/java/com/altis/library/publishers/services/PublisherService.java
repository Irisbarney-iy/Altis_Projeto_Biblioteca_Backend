package com.altis.library.publishers.services;

import com.altis.library.books.repositories.BookRepository;
import com.altis.library.publishers.mappers.PublisherMapper;
import com.altis.library.publishers.models.dtos.PublisherCreateRequest;
import com.altis.library.publishers.models.dtos.PublisherResponse;
import com.altis.library.publishers.models.dtos.PublisherUpdateRequest;
import com.altis.library.publishers.models.entities.PublisherEntity;
import com.altis.library.publishers.repositories.PublisherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;
    private final BookRepository bookRepository;
    private final PublisherMapper publisherMapper;

    public PublisherService(PublisherRepository publisherRepository, BookRepository bookRepository, PublisherMapper publisherMapper) {
        this.publisherRepository = publisherRepository;
        this.bookRepository = bookRepository;
        this.publisherMapper = publisherMapper;
    }

    @Transactional
    public PublisherResponse create(PublisherCreateRequest request) {
        if (publisherRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("E-mail já cadastrado!");
        }
        if (publisherRepository.existsByName(request.name())) {
            throw new IllegalArgumentException("Nome de editora já cadastrada!");
        }
        PublisherEntity publisher = publisherMapper.toEntity(request);
        PublisherEntity savedPublisher = publisherRepository.save(publisher);

        return publisherMapper.toResponse(savedPublisher);
    }

    @Transactional
    public PublisherResponse update(Long id, PublisherUpdateRequest request) {
        PublisherEntity publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Editora não encontrada!"));

        if (request.email() != null && !request.email().isBlank()) {
            if (!publisher.getEmail().equalsIgnoreCase(request.email()) && publisherRepository.existsByEmail(request.email())) {
                throw new IllegalArgumentException("Esse e-mail já está em uso!");
            }
        }

        if (request.name() != null && !request.name().isBlank()) {
            if (!publisher.getName().equalsIgnoreCase(request.name()) && publisherRepository.existsByName(request.name())) {
                throw new IllegalArgumentException("Esse nome já está em uso!");
            }
        }

        publisherMapper.updateEntityFromDto(request, publisher);
        PublisherEntity savedPublisher = publisherRepository.save(publisher);

        return publisherMapper.toResponse(savedPublisher);
    }

    @Transactional(readOnly = true)
    public PublisherResponse findById(Long id) {
        PublisherEntity publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Editora não encontrada"));
        return publisherMapper.toResponse(publisher);
    }

    @Transactional(readOnly = true)
    public Page<PublisherResponse> search(String term, Pageable pageable) {
        return publisherRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                term, term, pageable).map(publisherMapper::toResponse);
    }

    @Transactional
    public void delete(Long id) {
        publisherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Editora não encontrada"));
        if (bookRepository.existsByPublisherId(id)) {
            throw new IllegalStateException("Não é possível excluir a editora pois há vínculo com ela!");
        }

        publisherRepository.deleteById(id);
    }
}