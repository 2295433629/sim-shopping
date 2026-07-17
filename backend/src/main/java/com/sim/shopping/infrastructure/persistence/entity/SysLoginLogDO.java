package com.sim.shopping.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * SysLoginLog数据实体，对应数据库表
 *
 * @author Sim Team
 * @since 1.0.0
 */
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
     * 获取Error Msg
     * @return 返回结果
     */
    public String getErrorMsg() { return this.errorMsg; }
    /**
     * set Error Msg
     * @param errorMsg errorMsg
     */
    public void setErrorMsg(String errorMsg) { this.errorMsg = errorMsg; }
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
     * 获取Ip
     * @return 返回结果
     */
    public String getIp() { return this.ip; }
    /**
     * set Ip
     * @param ip ip
     */
    public void setIp(String ip) { this.ip = ip; }
}
