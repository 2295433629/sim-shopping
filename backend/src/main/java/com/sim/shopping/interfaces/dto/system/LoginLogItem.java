package com.sim.shopping.interfaces.dto.system;

import java.time.LocalDateTime;

/**
 * LoginLogItem
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class LoginLogItem {
    private Long id;
    private Long userId;
    private String username;
    private String userType;
    private Integer status;
    private String ip;
    private String loginType;
    private String userAgent;
    private String errorMsg;
    private LocalDateTime createdAt;

    /** 获取Id */
    public Long getId() { return this.id; }
    /** set Id */
    public void setId(Long id) { this.id = id; }
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
     * 获取User Type
     * @return 返回结果
     */
    public String getUserType() { return this.userType; }
    /**
     * set User Type
     * @param userType userType
     */
    public void setUserType(String userType) { this.userType = userType; }
    /**
     * 获取Status
     * @return 返回结果
     */
    public Integer getStatus() { return this.status; }
    /**
     * set Status
     * @param status status
     */
    public void setStatus(Integer status) { this.status = status; }
    /**
     * 获取Ip
     * @return 返回结果
     */
    public String getIp() { return this.ip; }
    /**
     * set Ip
     * @param ip ip
     */
    public void setIp(String ip) { this.ip = ip; }
    /**
     * 获取Login Type
     * @return 返回结果
     */
    public String getLoginType() { return this.loginType; }
    /**
     * set Login Type
     * @param loginType loginType
     */
    public void setLoginType(String loginType) { this.loginType = loginType; }
    /**
     * 获取User Agent
     * @return 返回结果
     */
    public String getUserAgent() { return this.userAgent; }
    /**
     * set User Agent
     * @param userAgent userAgent
     */
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
    /**
     * 获取Error Msg
     * @return 返回结果
     */
    public String getErrorMsg() { return this.errorMsg; }
    /**
     * set Error Msg
     * @param errorMsg errorMsg
     */
    public void setErrorMsg(String errorMsg) { this.errorMsg = errorMsg; }
    /** 获取Created At */
    public LocalDateTime getCreatedAt() { return this.createdAt; }
    /** set Created At */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
