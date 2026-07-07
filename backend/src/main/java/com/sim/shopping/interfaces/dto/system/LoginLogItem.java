package com.sim.shopping.interfaces.dto.system;

import java.time.LocalDateTime;

public class LoginLogItem {
    private Long id;
    private Long userId;
    private String username;
    private String userType;
    private Integer status;
    private String ip;
    private String loginType;
    private String userAgent;
    private String errorMsg;
    private LocalDateTime createdAt;

    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return this.userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return this.username; }
    public void setUsername(String username) { this.username = username; }
    public String getUserType() { return this.userType; }
    public void setUserType(String userType) { this.userType = userType; }
    public Integer getStatus() { return this.status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getIp() { return this.ip; }
    public void setIp(String ip) { this.ip = ip; }
    public String getLoginType() { return this.loginType; }
    public void setLoginType(String loginType) { this.loginType = loginType; }
    public String getUserAgent() { return this.userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
    public String getErrorMsg() { return this.errorMsg; }
    public void setErrorMsg(String errorMsg) { this.errorMsg = errorMsg; }
    public LocalDateTime getCreatedAt() { return this.createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
