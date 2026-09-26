package com.altis.library.dashboard.controllers;

import com.altis.library.dashboard.models.dtos.AdminDashboardResponse;
import com.altis.library.dashboard.models.dtos.UserDashboardResponse;
import com.altis.library.dashboard.services.DashboardService;
import com.altis.library.users.models.entities.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminDashboardResponse> getAdminDashboard() {
        return ResponseEntity.ok(dashboardService.getAdminDashboard());
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserDashboardResponse> getUserDashboard(Authentication authentication) {
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        return ResponseEntity.ok(dashboardService.getUserDashboard(currentUser.getId()));
    }
}