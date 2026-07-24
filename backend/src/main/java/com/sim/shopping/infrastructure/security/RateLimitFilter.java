package com.sim.shopping.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 登录接口简单限流过滤器
 * 使用 ConcurrentHashMap + 滑动窗口实现内存限流
 * 每个IP每分钟最多10次登录请求
 * 注意：不使用@Component，由SecurityConfig作为Bean创建，避免Spring Security 6.x的Filter order注册问题
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class RateLimitFilter extends OncePerRequestFilter implements Ordered {

    private static final Logger log = LoggerFactory.getLogger(RateLimitFilter.class);
    private static final int FILTER_ORDER = Ordered.HIGHEST_PRECEDENCE + 9;

    private final ConcurrentHashMap<String, LinkedList<Long>> loginAttempts = new ConcurrentHashMap<>();
    private static final int MAX_ATTEMPTS = 10;
    private static final long WINDOW_MS = 60_000;

    private final ObjectMapper objectMapper;

    public RateLimitFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        // 只对登录接口进行限流
        if ("/api/common/login".equals(requestURI) || "/api/admin/login".equals(requestURI)) {
            String ip = getClientIp(request);

            if (isRateLimited(ip)) {
                log.warn("Rate limit exceeded for ip={}, uri={}", ip, requestURI);
                response.setStatus(429);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("UTF-8");
                objectMapper.writeValue(response.getOutputStream(),
                        ApiResponse.error(429, "请求过于频繁，请稍后再试"));
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 检查是否被限流
     * @param ip 客户端IP
     * @return true表示被限流，false表示允许通过
     */
    private boolean isRateLimited(String ip) {
        long now = System.currentTimeMillis();
        long windowStart = now - WINDOW_MS;

        loginAttempts.computeIfAbsent(ip, k -> new LinkedList<>());

        LinkedList<Long> attempts = loginAttempts.get(ip);

        // 清理窗口外的旧记录
        synchronized (attempts) {
            Iterator<Long> iterator = attempts.iterator();
            while (iterator.hasNext()) {
                if (iterator.next() < windowStart) {
                    iterator.remove();
                } else {
                    break;
                }
            }

            if (attempts.size() >= MAX_ATTEMPTS) {
                return true;
            }

            attempts.addLast(now);
            return false;
        }
    }

    /**
     * 获取客户端真实IP
     * @param request HTTP请求
     * @return 客户端IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            // 多次代理时取第一个IP
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index).trim();
            }
            return ip.trim();
        }
        ip = request.getHeader("X-Real-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip.trim();
        }
        return request.getRemoteAddr();
    }

    @Override
    public int getOrder() {
        return FILTER_ORDER;
    }
}
