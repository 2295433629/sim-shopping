package com.sim.shopping.interfaces.dto.auth;

/**
 * Token响应对象，封装接口出参
 *
 * @author Sim Team
 * @since 1.0.0
 */
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
     * 获取Avatar
     * @return 返回结果
     */
    public String getAvatar() { return this.avatar; }
    /**
     * set Avatar
     * @param avatar avatar
     */
    public void setAvatar(String avatar) { this.avatar = avatar; }
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
     * 获取Token
     * @return 返回结果
     */
    public String getToken() { return this.token; }
    /**
     * set Token
     * @param token token
     */
    public void setToken(String token) { this.token = token; }
    /**
     * 获取Expires In
     * @return 返回结果
     */
    public long getExpiresIn() { return this.expiresIn; }
    /**
     * set Expires In
     * @param expiresIn expiresIn
     */
    public void setExpiresIn(long expiresIn) { this.expiresIn = expiresIn; }
    /**
     * 获取Refresh Token
     * @return 返回结果
     */
    public String getRefreshToken() { return this.refreshToken; }
    /**
     * set Refresh Token
     * @param refreshToken refreshToken
     */
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}
