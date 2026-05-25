package com.loginmonitoring.backend.controller;

import com.loginmonitoring.backend.model.AuthRequest;
import com.loginmonitoring.backend.model.AuthResponse;
import com.loginmonitoring.backend.model.RegisterRequest;
import com.loginmonitoring.backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request, HttpServletRequest httpRequest) {
        String ip = extractClientIp(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");
        return ResponseEntity.ok(authService.login(request, ip, userAgent == null ? "Unknown Browser" : userAgent));
    }

    @PostMapping("/unlock/{username}")
    public ResponseEntity<Map<String, String>> unlock(@PathVariable String username) {
        return ResponseEntity.ok(authService.unlockUser(username));
    }

    private String extractClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isEmpty()) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0].trim();
    }
}
