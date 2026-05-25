package com.loginmonitoring.backend.controller;

import com.loginmonitoring.backend.model.BlockedUser;
import com.loginmonitoring.backend.model.LoginLog;
import com.loginmonitoring.backend.service.AuthService;
import com.loginmonitoring.backend.service.LoginMonitoringService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final LoginMonitoringService monitoringService;
    private final AuthService authService;

    public AdminController(LoginMonitoringService monitoringService, AuthService authService) {
        this.monitoringService = monitoringService;
        this.authService = authService;
    }

    @GetMapping("/logs")
    public ResponseEntity<List<LoginLog>> logs() {
        return ResponseEntity.ok(monitoringService.getRecentLogs());
    }

    @GetMapping("/blocked-users")
    public ResponseEntity<List<BlockedUser>> blockedUsers() {
        return ResponseEntity.ok(monitoringService.getBlockedUsers());
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Long>> summary() {
        return ResponseEntity.ok(monitoringService.getDashboardSummary());
    }

    @PutMapping("/unblock/{username}")
    public ResponseEntity<Map<String, String>> unblock(@PathVariable String username) {
        return ResponseEntity.ok(authService.unlockUser(username));
    }
}
