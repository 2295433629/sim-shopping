package com.sim.shopping.interfaces.dto.system;

import java.time.LocalDateTime;

public class AdminUserItem {
    private Long userId;
    private String username;
    private String nickname;
    private String phone;
    private String email;
    private String role;
    private String status;
    private Integer points;
    private LocalDateTime createdAt;

    public Long getUserId() { return this.userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return this.username; }
    public void setUsername(String username) { this.username = username; }
    public String getNickname() { return this.nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getPhone() { return this.phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return this.email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return this.role; }
    public void setRole(String role) { this.role = role; }
    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getPoints() { return this.points; }
    public void setPoints(Integer points) { this.points = points; }
    public LocalDateTime getCreatedAt() { return this.createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
