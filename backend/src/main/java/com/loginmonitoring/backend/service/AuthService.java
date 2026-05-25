package com.loginmonitoring.backend.service;

import com.loginmonitoring.backend.model.*;
import com.loginmonitoring.backend.repository.UserRepository;
import com.loginmonitoring.backend.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class AuthService {

    private static final int MAX_FAILED_ATTEMPTS = 5;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final LoginMonitoringService loginMonitoringService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService,
                       LoginMonitoringService loginMonitoringService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.loginMonitoringService = loginMonitoringService;
    }

    public Map<String, String> register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);

        return Map.of("message", "User registered successfully");
    }

    public AuthResponse login(AuthRequest request, String ipAddress, String browserDetails) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (user.isAccountLocked()) {
            loginMonitoringService.saveLoginAttempt(user.getUsername(), ipAddress, browserDetails, LoginStatus.BLOCKED);
            throw new IllegalStateException("Account is locked due to too many failed login attempts");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            user.setFailedLoginAttempts(0);
            userRepository.save(user);
            loginMonitoringService.saveLoginAttempt(user.getUsername(), ipAddress, browserDetails, LoginStatus.SUCCESS);

            String token = jwtService.generateToken(
                    org.springframework.security.core.userdetails.User
                            .withUsername(user.getUsername())
                            .password(user.getPassword())
                            .roles(user.getRole().name().replace("ROLE_", ""))
                            .build()
            );

            return new AuthResponse(token, user.getUsername(), user.getRole().name());
        } catch (BadCredentialsException ex) {
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);

            if (user.getFailedLoginAttempts() >= MAX_FAILED_ATTEMPTS) {
                user.setAccountLocked(true);
                user.setLockTime(LocalDateTime.now());
                loginMonitoringService.blockUser(
                        user.getUsername(),
                        user.getFailedLoginAttempts(),
                        "Exceeded 5 failed login attempts"
                );
            }

            userRepository.save(user);
            loginMonitoringService.saveLoginAttempt(user.getUsername(), ipAddress, browserDetails, LoginStatus.FAILED);
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    public Map<String, String> unlockUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setAccountLocked(false);
        user.setFailedLoginAttempts(0);
        user.setLockTime(null);
        userRepository.save(user);
        loginMonitoringService.unblockUser(username);
        return Map.of("message", "User unlocked successfully");
    }
}
