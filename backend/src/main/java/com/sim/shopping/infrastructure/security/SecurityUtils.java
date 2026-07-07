package com.sim.shopping.infrastructure.security;

import com.sim.shopping.domain.common.exception.BusinessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof SecurityUser) {
            return ((SecurityUser) auth.getPrincipal()).getUserId();
        }
        throw new BusinessException(401, "未登录");
    }

    public static String getCurrentUserRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof SecurityUser) {
            return ((SecurityUser) auth.getPrincipal()).getUserType();
        }
        throw new BusinessException(401, "未登录");
    }

    public static SecurityUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof SecurityUser) {
            return (SecurityUser) auth.getPrincipal();
        }
        throw new BusinessException(401, "未登录");
    }
}
