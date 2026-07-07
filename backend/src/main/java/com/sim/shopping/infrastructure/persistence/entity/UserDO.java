package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;
@TableName("t_user")
public class UserDO extends BaseEntity {
    private String username;
    private String password;
    private String nickname;
    private String avatar;
    private Integer gender;
    private String phone;
    private String email;
    private String role;
    private String status;
    private Integer points;
    // userType derived from role for SecurityUser
    public String getUserType() { return role; }

    public String getUsername() { return this.username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return this.password; }
    public void setPassword(String password) { this.password = password; }
    public String getNickname() { return this.nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getAvatar() { return this.avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public Integer getGender() { return this.gender; }
    public void setGender(Integer gender) { this.gender = gender; }
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
}
