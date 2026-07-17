package com.sim.shopping.infrastructure.security;

import com.sim.shopping.infrastructure.persistence.entity.UserDO;
import com.sim.shopping.infrastructure.persistence.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * UserDetailsServiceImpl
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    public UserDetailsServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 加载User By Username
     * @param username username
     * @return 返回结果
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDO user = userMapper.selectOne(
                com.baomidou.mybatisplus.core.toolkit.Wrappers.<UserDO>lambdaQuery()
                        .eq(UserDO::getPhone, username)
                        .or()
                        .eq(UserDO::getEmail, username)
                        .or()
                        .eq(UserDO::getUsername, username)
                        .last("LIMIT 1")
        );
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }
        return new SecurityUser(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRole(),
                user.getRole(),
                "ACTIVE".equals(user.getStatus())
        );
    }

}
