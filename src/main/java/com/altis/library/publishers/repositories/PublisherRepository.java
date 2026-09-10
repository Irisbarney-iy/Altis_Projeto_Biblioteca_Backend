package com.altis.library.publishers.repositories;

import com.altis.library.publishers.models.entities.PublisherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<PublisherEntity, Long> {

    boolean existsByName(String name);
}