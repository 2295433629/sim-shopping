package com.sim.shopping.interfaces.dto.auth;

public class UserInfoResponse {

    private Long userId;
    private String username;
    private String nickname;
    private String avatar;
    private Integer gender;
    private String phone;
    private String email;
    private String role;
    private Integer points;
    private Long merchantId;

    public UserInfoResponse() {
    }

    public Long getUserId() { return this.userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return this.username; }
    public void setUsername(String username) { this.username = username; }
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
    public Integer getPoints() { return this.points; }
    public void setPoints(Integer points) { this.points = points; }
    public Long getMerchantId() { return this.merchantId; }
    public void setMerchantId(Long merchantId) { this.merchantId = merchantId; }
}
