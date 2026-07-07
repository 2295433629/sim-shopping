package com.sim.shopping.interfaces.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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
}
