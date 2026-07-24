package com.sim.shopping.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sim.shopping.interfaces.dto.common.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security配置类，配置JWT认证、权限控制和过滤器链
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.profiles.active:local}")
    private String activeProfile;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider,
                         ObjectMapper objectMapper) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    /**
     * security Filter Chain
     * @param http http
     * @return 返回结果
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> {
                        auth
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/api/common/register").permitAll()
                        .requestMatchers("/api/common/login").permitAll()
                        .requestMatchers("/api/common/refresh-token").permitAll()
                        .requestMatchers("/api/admin/login").permitAll()
                        .requestMatchers("/uploads/**").permitAll()
                        .requestMatchers("/uploads/private/**").authenticated()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                        if (!"prod".equals(activeProfile) && !"production".equals(activeProfile)) {
                            auth.requestMatchers("/swagger-ui/**").permitAll()
                               .requestMatchers("/v3/api-docs/**").permitAll();
                        }
                        auth.anyRequest().authenticated();
                })
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setCharacterEncoding("UTF-8");
                            objectMapper.writeValue(response.getOutputStream(),
                                    ApiResponse.error(401, "未登录或认证已过期"));
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setCharacterEncoding("UTF-8");
                            objectMapper.writeValue(response.getOutputStream(),
                                    ApiResponse.error(403, "无权限访问该资源"));
                        })
                );

        // 在UsernamePasswordAuthenticationFilter前插入自定义Filter
        // 先添加的在后面，后添加的在前面（都相对于UsernamePasswordAuthenticationFilter）
        http.addFilterBefore(new RateLimitFilter(objectMapper), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, redisTemplate), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * password Encoder
     * @return 返回结果
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * authentication Manager
     * @param config config
     * @return 返回结果
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
