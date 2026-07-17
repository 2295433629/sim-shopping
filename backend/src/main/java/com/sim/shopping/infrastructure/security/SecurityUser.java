package com.sim.shopping.infrastructure.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
/**
 * SecurityUser
 *
 * @author Sim Team
 * @since 1.0.0
 */
public class SecurityUser implements UserDetails {

    private final Long userId;
    private final String username;
    private final String password;
    private final String userType;
    private final String role;
    private final boolean enabled;
    private final Collection<? extends GrantedAuthority> authorities;

    public SecurityUser(Long userId, String username, String password, String userType, String role, boolean enabled) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.role = role;
        this.enabled = enabled;
        this.authorities = role != null
                ? Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                : Collections.emptyList();
    }

    /**
     * 获取User Id
     * @return 返回结果
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 获取User Type
     * @return 返回结果
     */
    public String getUserType() {
        return userType;
    }

    /**
     * 获取Role
     * @return 返回结果
     */
    public String getRole() {
        return role;
    }

    /**
     * 获取Authorities
     * @return 返回结果
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * 获取Password
     * @return 返回结果
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * 获取Username
     * @return 返回结果
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * is Account Non Expired
     * @return 返回结果
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * is Account Non Locked
     * @return 返回结果
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * is Credentials Non Expired
     * @return 返回结果
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * is Enabled
     * @return 返回结果
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
