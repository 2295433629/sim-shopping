package com.sim.shopping.interfaces.dto.auth;

/**
 * UserInfo响应对象，封装接口出参
 *
 * @author Sim Team
 * @since 1.0.0
 */
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
     * 获取Gender
     * @return 返回结果
     */
    public Integer getGender() { return this.gender; }
    /**
     * set Gender
     * @param gender gender
     */
    public void setGender(Integer gender) { this.gender = gender; }
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
     * 获取Points
     * @return 返回结果
     */
    public Integer getPoints() { return this.points; }
    /**
     * set Points
     * @param points points
     */
    public void setPoints(Integer points) { this.points = points; }
    /**
     * 获取Merchant Id
     * @return 返回结果
     */
    public Long getMerchantId() { return this.merchantId; }
    /**
     * set Merchant Id
     * @param merchantId merchantId
     */
    public void setMerchantId(Long merchantId) { this.merchantId = merchantId; }
}
