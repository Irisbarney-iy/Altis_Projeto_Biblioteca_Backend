package com.altis.library.publishers.controllers;

import com.altis.library.publishers.models.dtos.PublisherCreateRequest;
import com.altis.library.publishers.models.dtos.PublisherResponse;
import com.altis.library.publishers.models.dtos.PublisherUpdateRequest;
import com.altis.library.publishers.services.PublisherService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService){
        this.publisherService = publisherService;
    }

    @PostMapping
    public ResponseEntity<PublisherResponse> create(@RequestBody @Valid PublisherCreateRequest request){
        PublisherResponse response = publisherService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PublisherResponse> update(
            @PathVariable Long id, @RequestBody @Valid PublisherUpdateRequest request
    ) {
        PublisherResponse response = publisherService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherResponse> findById(@PathVariable Long id){
        PublisherResponse response = publisherService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PublisherResponse>> search(
            @RequestParam(required = false) String term,
            @ParameterObject @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable
    ){
        Page<PublisherResponse> response = publisherService.search(term, pageable);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        publisherService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
