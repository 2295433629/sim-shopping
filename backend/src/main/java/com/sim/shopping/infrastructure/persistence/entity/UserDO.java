package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 用户实体，对应 t_user 表，存储用户账户信息
 *
 * @author Sim Team
 * @since 1.0.0
 */
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
    /**
     * 获取User Type
     * @return 返回结果
     */
    public String getUserType() { return role; }

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
     * 获取Password
     * @return 返回结果
     */
    public String getPassword() { return this.password; }
    /**
     * set Password
     * @param password password
     */
    public void setPassword(String password) { this.password = password; }
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
}
