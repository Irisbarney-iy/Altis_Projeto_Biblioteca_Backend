package com.altis.library.users.controllers;

import com.altis.library.users.models.dtos.UserCreateRequest;
import com.altis.library.users.models.dtos.UserResponse;
import com.altis.library.users.models.dtos.UserUpdateRequest;
import com.altis.library.users.services.UserService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UserCreateRequest request, UriComponentsBuilder uriBuilder) {
        UserResponse response = userService.create(request);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @PatchMapping("/me")
    public ResponseEntity<UserResponse> updateMe(Authentication authentication, @Valid @RequestBody UserUpdateRequest request) {
        String email = authentication.getName();
        return ResponseEntity.ok(userService.updateByEmail(email, request));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponse>> search(
            @RequestParam(required = false, defaultValue = "") String term,
            @ParameterObject @PageableDefault(size = 10, sort = "name") Pageable pageable
    ) {
        return ResponseEntity.ok(userService.search(term, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PatchMapping("/{id}/inactive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> inactive(@PathVariable Long id) {
        userService.inactive(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        userService.activate(id);
        return ResponseEntity.noContent().build();
    }
}