package com.loginmonitoring.backend.repository;

import com.loginmonitoring.backend.model.BlockedUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlockedUserRepository extends JpaRepository<BlockedUser, Long> {
    Optional<BlockedUser> findByUsername(String username);
    void deleteByUsername(String username);
}
