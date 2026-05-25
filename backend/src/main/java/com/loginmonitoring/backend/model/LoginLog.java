package com.loginmonitoring.backend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "login_logs")
public class LoginLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(nullable = false, length = 45)
    private String ipAddress;

    @Column(nullable = false, length = 255)
    private String browserDetails;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LoginStatus loginStatus;

    @Column(nullable = false)
    private LocalDateTime loginTime = LocalDateTime.now();

    @Column(nullable = false)
    private boolean suspicious = false;

    @Column(length = 255)
    private String suspiciousReason;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getBrowserDetails() {
        return browserDetails;
    }

    public void setBrowserDetails(String browserDetails) {
        this.browserDetails = browserDetails;
    }

    public LoginStatus getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(LoginStatus loginStatus) {
        this.loginStatus = loginStatus;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public boolean isSuspicious() {
        return suspicious;
    }

    public void setSuspicious(boolean suspicious) {
        this.suspicious = suspicious;
    }

    public String getSuspiciousReason() {
        return suspiciousReason;
    }

    public void setSuspiciousReason(String suspiciousReason) {
        this.suspiciousReason = suspiciousReason;
    }
}
