package com.loginmonitoring.backend.repository;

import com.loginmonitoring.backend.model.LoginLog;
import com.loginmonitoring.backend.model.LoginStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {
    List<LoginLog> findTop100ByOrderByLoginTimeDesc();
    long countByLoginStatus(LoginStatus status);
    long countByIpAddressAndLoginStatusAndLoginTimeAfter(String ipAddress, LoginStatus status, LocalDateTime after);
    long countByUsernameAndLoginStatusAndLoginTimeAfter(String username, LoginStatus status, LocalDateTime after);
}
