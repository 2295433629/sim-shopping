package com.sim.shopping.interfaces.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * UpdateProfile请求对象，封装接口入参
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class UpdateProfileRequest {

    @NotBlank(message = "昵称不能为空")
    @Size(max = 50, message = "昵称长度不能超过50")
    private String nickname;

    private String avatar;

    private Integer gender;

    @Size(max = 20, message = "手机号长度不能超过20")
    private String phone;

    @Size(max = 100, message = "邮箱长度不能超过100")
    private String email;

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
}
