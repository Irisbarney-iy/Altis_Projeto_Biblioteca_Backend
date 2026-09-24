package com.altis.library.publishers.mappers;

import com.altis.library.publishers.models.dtos.PublisherCreateRequest;
import com.altis.library.publishers.models.dtos.PublisherResponse;
import com.altis.library.publishers.models.dtos.PublisherUpdateRequest;
import com.altis.library.publishers.models.entities.PublisherEntity;
import org.springframework.stereotype.Component;

@Component
public class PublisherMapper {

    public PublisherEntity toEntity(PublisherCreateRequest request) {
        if (request == null) return null;

        PublisherEntity publisher = new PublisherEntity();
        publisher.setName(request.name());
        publisher.setEmail(request.email());

        if (request.phone() != null) {
            publisher.setPhone(request.phone().replaceAll("\\D", ""));
        }

        publisher.setSite(request.site());

        return publisher;
    }

    public void updateEntityFromDto(PublisherUpdateRequest request, PublisherEntity publisher) {
        if (request == null || publisher == null) return;

        if (request.email() != null && !request.email().isBlank()) {
            publisher.setEmail(request.email());
        }
        if (request.name() != null && !request.name().isBlank()) {
            publisher.setName(request.name());
        }
        if (request.phone() != null && !request.phone().isBlank()) {
            publisher.setPhone(request.phone().replaceAll("\\D", ""));
        }
        if (request.site() != null) {
            publisher.setSite(request.site());
        }
    }

    public PublisherResponse toResponse(PublisherEntity entity) {
        if (entity == null) return null;

        return new PublisherResponse(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getSite()
        );
    }
}