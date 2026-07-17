package com.sim.shopping.interfaces.dto.system;

import java.time.LocalDateTime;

/**
 * AdminUserItem
 *
 * @author Sim Team
 * @since 1.0.0
 */
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

    /**
     * 获取User Id
     * @return 返回结果
     */
    public Long getUserId() { return this.userId; }
    /**
     * set User Id
     * @param userId userId
     */
    public void setUserId(Long userId) { this.userId = userId; }
    /**
     * 获取Username
     * @return 返回结果
     */
    public String getUsername() { return this.username; }
    /**
     * set Username
     * @param username username
     */
    public void setUsername(String username) { this.username = username; }
    /**
     * 获取Nickname
     * @return 返回结果
     */
    public String getNickname() { return this.nickname; }
    /**
     * set Nickname
     * @param nickname nickname
     */
    public void setNickname(String nickname) { this.nickname = nickname; }
    /**
     * 获取Phone
     * @return 返回结果
     */
    public String getPhone() { return this.phone; }
    /**
     * set Phone
     * @param phone phone
     */
    public void setPhone(String phone) { this.phone = phone; }
    /**
     * 获取Email
     * @return 返回结果
     */
    public String getEmail() { return this.email; }
    /**
     * set Email
     * @param email email
     */
    public void setEmail(String email) { this.email = email; }
    /**
     * 获取Role
     * @return 返回结果
     */
    public String getRole() { return this.role; }
    /**
     * set Role
     * @param role role
     */
    public void setRole(String role) { this.role = role; }
    /**
     * 获取Status
     * @return 返回结果
     */
    public String getStatus() { return this.status; }
    /**
     * set Status
     * @param status status
     */
    public void setStatus(String status) { this.status = status; }
    /**
     * 获取Points
     * @return 返回结果
     */
    public Integer getPoints() { return this.points; }
    /**
     * set Points
     * @param points points
     */
    public void setPoints(Integer points) { this.points = points; }
    /** 获取Created At */
    public LocalDateTime getCreatedAt() { return this.createdAt; }
    /** set Created At */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
