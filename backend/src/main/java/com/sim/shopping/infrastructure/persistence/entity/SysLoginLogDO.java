package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
@TableName("t_sys_login_log")
public class SysLoginLogDO extends BaseEntity {
    private String username;
    private Long userId;
    private String userType;
    private Integer status;
    private String errorMsg;
    private String userAgent;
    private String loginType;
    private String ip;

    public String getUsername() { return this.username; }
    public void setUsername(String username) { this.username = username; }
    public Long getUserId() { return this.userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUserType() { return this.userType; }
    public void setUserType(String userType) { this.userType = userType; }
    public Integer getStatus() { return this.status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getErrorMsg() { return this.errorMsg; }
    public void setErrorMsg(String errorMsg) { this.errorMsg = errorMsg; }
    public String getUserAgent() { return this.userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
    public String getLoginType() { return this.loginType; }
    public void setLoginType(String loginType) { this.loginType = loginType; }
    public String getIp() { return this.ip; }
    public void setIp(String ip) { this.ip = ip; }
}
