package com.altis.library.publishers.services;

import com.altis.library.books.repositories.BookRepository;
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

    public PublisherService(PublisherRepository publisherRepository, BookRepository bookRepository) {
        this.publisherRepository = publisherRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public PublisherResponse create(PublisherCreateRequest request){
        if(publisherRepository.existsByEmail(request.email())){
            throw new IllegalArgumentException("E-mail já cadastrado!");
        }

        if(publisherRepository.existsByName(request.name())){
            throw new IllegalArgumentException("Nome de editora já cadastrada!");
        }

        PublisherEntity publisher = new PublisherEntity();
        publisher.setName(request.name());
        publisher.setEmail(request.email());
        String phoneNumberOnly = request.phone().replaceAll("\\D","");
        publisher.setPhone(phoneNumberOnly);
        publisher.setSite(request.site());

        PublisherEntity savedPublisher = publisherRepository.save(publisher);
        return toResponse(savedPublisher);

    }

    @Transactional
    public PublisherResponse update(Long id, PublisherUpdateRequest request){
        PublisherEntity publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Editora não encontrada!"));
        if(request.email() != null && !request.email().isBlank()){
            if(!publisher.getEmail().equalsIgnoreCase(request.email()) && publisherRepository.existsByEmail(request.email())){
                throw new IllegalArgumentException("Esse e-mail já está em uso!");
            }
            publisher.setEmail(request.email());
        }

        if(request.name() != null && !request.name().isBlank()){
            if(!publisher.getName().equalsIgnoreCase(request.name()) && publisherRepository.existsByName(request.name())){
                throw new IllegalArgumentException("Esse nome já está em uso!");
            }
            publisher.setName(request.name());
        }

        if(request.phone() != null && !request.phone().isBlank()){
            publisher.setPhone(request.phone().replaceAll("\\D", ""));
        }

        if(request.site() != null){
            publisher.setSite(request.site());
        }

        PublisherEntity savedPublisher = publisherRepository.save(publisher);
        return toResponse(savedPublisher);
    }

    @Transactional(readOnly = true)
    public PublisherResponse findById(Long id){
        PublisherEntity publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Editora não encontrado"));
        return toResponse(publisher);
    }

    @Transactional(readOnly = true)
    public Page<PublisherResponse> search(String term, Pageable pageable){
        return publisherRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                term, term, pageable).map(this::toResponse);
    }

    @Transactional
    public void delete(Long id){
        PublisherEntity publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Editora não encontrada"));

        if(bookRepository.existsByPublisherId(id)){
            throw new IllegalStateException("Não é possivel excluir a editora pois há vinculo com ela!");
        }

        publisherRepository.deleteById(id);
    }

    private PublisherResponse toResponse(PublisherEntity entity){
        return new PublisherResponse(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getSite()
        );
    }

}
