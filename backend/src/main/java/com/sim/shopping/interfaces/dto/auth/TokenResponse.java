package com.sim.shopping.interfaces.dto.auth;

public class TokenResponse {

    private Long userId;
    private String username;
    private String nickname;
    private String avatar;
    private String role;
    private String token;
    private long expiresIn;
    private String refreshToken;

    public TokenResponse() {
    }

    /**
     * 兼容旧构造函数：仅返回 token 信息（不含昵称/头像/角色）。
     */
    public TokenResponse(Long userId, String username, String token, long expiresIn, String refreshToken) {
        this.userId = userId;
        this.username = username;
        this.token = token;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
    }

    public TokenResponse(Long userId,
                         String username,
                         String nickname,
                         String avatar,
                         String role,
                         String token,
                         long expiresIn,
                         String refreshToken) {
        this.userId = userId;
        this.username = username;
        this.nickname = nickname;
        this.avatar = avatar;
        this.role = role;
        this.token = token;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
    }

    public Long getUserId() { return this.userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return this.username; }
    public void setUsername(String username) { this.username = username; }
    public String getNickname() { return this.nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getAvatar() { return this.avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public String getRole() { return this.role; }
    public void setRole(String role) { this.role = role; }
    public String getToken() { return this.token; }
    public void setToken(String token) { this.token = token; }
    public long getExpiresIn() { return this.expiresIn; }
    public void setExpiresIn(long expiresIn) { this.expiresIn = expiresIn; }
    public String getRefreshToken() { return this.refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}
