package com.sim.shopping.infrastructure.security;

import com.sim.shopping.application.auth.AuthService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
/**
 * JWT认证过滤器，拦截请求并验证JWT Token，同时检查Token黑名单
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, AuthService authService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            try {
                // 检查Token是否在黑名单中（已登出或被吊销）
                if (authService.isTokenBlacklisted(token)) {
                    log.debug("Token is blacklisted");
                    SecurityContextHolder.clearContext();
                } else {
                    Claims claims = jwtTokenProvider.parseToken(token);
                    String tokenType = claims.get("tokenType", String.class);
                    if ("access".equals(tokenType)) {
                        String subject = claims.getSubject();
                        if (subject == null) {
                            log.debug("Token subject is null");
                            SecurityContextHolder.clearContext();
                        } else {
                            Long userId = Long.parseLong(subject);
                            String username = claims.get("username", String.class);
                            String userType = claims.get("userType", String.class);

                            SecurityUser securityUser = new SecurityUser(userId, username, "", userType, userType, true);
                            UsernamePasswordAuthenticationToken authentication =
                                    new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                    }
                }
            } catch (Exception e) {
                log.debug("Failed to set authentication from token: {}", e.getMessage());
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
