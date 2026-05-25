package com.loginmonitoring.backend.service;

import com.loginmonitoring.backend.model.BlockedUser;
import com.loginmonitoring.backend.model.LoginLog;
import com.loginmonitoring.backend.model.LoginStatus;
import com.loginmonitoring.backend.repository.BlockedUserRepository;
import com.loginmonitoring.backend.repository.LoginLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoginMonitoringService {

    private final LoginLogRepository loginLogRepository;
    private final BlockedUserRepository blockedUserRepository;

    public LoginMonitoringService(LoginLogRepository loginLogRepository, BlockedUserRepository blockedUserRepository) {
        this.loginLogRepository = loginLogRepository;
        this.blockedUserRepository = blockedUserRepository;
    }

    public LoginLog saveLoginAttempt(String username, String ipAddress, String browser, LoginStatus status) {
        LoginLog log = new LoginLog();
        log.setUsername(username);
        log.setIpAddress(ipAddress);
        log.setBrowserDetails(browser);
        log.setLoginStatus(status);
        log.setLoginTime(LocalDateTime.now());

        evaluateSuspiciousActivity(log);
        return loginLogRepository.save(log);
    }

    private void evaluateSuspiciousActivity(LoginLog currentLog) {
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);
        long failedFromIp = loginLogRepository.countByIpAddressAndLoginStatusAndLoginTimeAfter(
                currentLog.getIpAddress(),
                LoginStatus.FAILED,
                tenMinutesAgo
        );
        long failedForUser = loginLogRepository.countByUsernameAndLoginStatusAndLoginTimeAfter(
                currentLog.getUsername(),
                LoginStatus.FAILED,
                tenMinutesAgo
        );

        if (failedFromIp >= 5) {
            currentLog.setSuspicious(true);
            currentLog.setSuspiciousReason("Potential brute-force: multiple failures from same IP");
        } else if (failedForUser >= 5) {
            currentLog.setSuspicious(true);
            currentLog.setSuspiciousReason("Potential account attack: repeated user login failures");
        }
    }

    public void blockUser(String username, int failedAttempts, String reason) {
        BlockedUser blockedUser = blockedUserRepository.findByUsername(username).orElse(new BlockedUser());
        blockedUser.setUsername(username);
        blockedUser.setFailedAttempts(failedAttempts);
        blockedUser.setBlockedAt(LocalDateTime.now());
        blockedUser.setReason(reason);
        blockedUserRepository.save(blockedUser);
    }

    public List<LoginLog> getRecentLogs() {
        return loginLogRepository.findTop100ByOrderByLoginTimeDesc();
    }

    public List<BlockedUser> getBlockedUsers() {
        return blockedUserRepository.findAll();
    }

    public Map<String, Long> getDashboardSummary() {
        Map<String, Long> summary = new HashMap<>();
        summary.put("successCount", loginLogRepository.countByLoginStatus(LoginStatus.SUCCESS));
        summary.put("failedCount", loginLogRepository.countByLoginStatus(LoginStatus.FAILED));
        summary.put("blockedCount", (long) blockedUserRepository.findAll().size());
        return summary;
    }

    public void unblockUser(String username) {
        blockedUserRepository.deleteByUsername(username);
    }
}
