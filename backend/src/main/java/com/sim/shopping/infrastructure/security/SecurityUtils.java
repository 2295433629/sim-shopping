package com.sim.shopping.infrastructure.security;

import com.sim.shopping.domain.common.exception.BusinessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 安全工具类，提供当前用户ID、用户名等安全上下文获取
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class SecurityUtils {

    /**
     * 获取Current User Id
     * @return 返回结果
     */
    public static Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof SecurityUser) {
            return ((SecurityUser) auth.getPrincipal()).getUserId();
        }
        throw new BusinessException(401, "未登录");
    }

    /**
     * 获取Current User Role
     * @return 返回结果
     */
    public static String getCurrentUserRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof SecurityUser) {
            return ((SecurityUser) auth.getPrincipal()).getUserType();
        }
        throw new BusinessException(401, "未登录");
    }

    /**
     * 获取Current User
     * @return 返回结果
     */
    public static SecurityUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof SecurityUser) {
            return (SecurityUser) auth.getPrincipal();
        }
        throw new BusinessException(401, "未登录");
    }
}
