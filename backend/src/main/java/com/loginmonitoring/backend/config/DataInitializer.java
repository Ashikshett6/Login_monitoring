package com.loginmonitoring.backend.config;

import com.loginmonitoring.backend.model.Role;
import com.loginmonitoring.backend.model.User;
import com.loginmonitoring.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@loginmonitoring.com");
                admin.setPassword(passwordEncoder.encode("Admin@123"));
                admin.setRole(Role.ROLE_ADMIN);
                userRepository.save(admin);
            }
        };
    }
}
