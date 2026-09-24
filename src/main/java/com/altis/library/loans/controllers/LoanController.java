package com.altis.library.loans.controllers;

import com.altis.library.loans.models.dtos.LoanCreateRequest;
import com.altis.library.loans.models.dtos.LoanRenewRequest;
import com.altis.library.loans.models.dtos.LoanResponse;
import com.altis.library.loans.services.LoanService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LoanResponse> create(@RequestBody @Valid LoanCreateRequest request) {
        LoanResponse response = loanService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanResponse> findById(@PathVariable Long id) {
        LoanResponse response = loanService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<LoanResponse>> search(
            @RequestParam(required = false, defaultValue = "") String term,
            @ParameterObject @PageableDefault(page = 0, size = 10, sort = "loanDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<LoanResponse> response = loanService.search(term, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LoanResponse>> findByUserId(@PathVariable Long userId) {
        List<LoanResponse> response = loanService.findByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/return")
    public ResponseEntity<LoanResponse> returnBook(@PathVariable Long id) {
        LoanResponse response = loanService.returnBook(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/renew")
    public ResponseEntity<LoanResponse> renew(
            @PathVariable Long id,
            @RequestBody @Valid LoanRenewRequest request
    ) {
        LoanResponse response = loanService.renew(id, request);
        return ResponseEntity.ok(response);
    }
}