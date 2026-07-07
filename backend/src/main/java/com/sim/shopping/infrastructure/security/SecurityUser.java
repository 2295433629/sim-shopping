package com.sim.shopping.infrastructure.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
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

    public Long getUserId() {
        return userId;
    }

    public String getUserType() {
        return userType;
    }

    public String getRole() {
        return role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
