package com.sim.shopping.application.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.domain.common.exception.UserException;
import com.sim.shopping.infrastructure.persistence.entity.SysAdminDO;
import com.sim.shopping.infrastructure.persistence.entity.UserDO;
import com.sim.shopping.infrastructure.persistence.mapper.SysAdminMapper;
import com.sim.shopping.infrastructure.persistence.mapper.UserMapper;
import com.sim.shopping.infrastructure.security.JwtTokenProvider;
import com.sim.shopping.interfaces.dto.system.AdminLoginRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理员认证服务，处理管理员登录和权限验证
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Service
public class AdminAuthService {

    private final SysAdminMapper sysAdminMapper;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AdminAuthService(SysAdminMapper sysAdminMapper,
                            UserMapper userMapper,
                            PasswordEncoder passwordEncoder,
                            JwtTokenProvider jwtTokenProvider) {
        this.sysAdminMapper = sysAdminMapper;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 管理员登录
     * @param req req
     * @return 返回结果
     */
    public Map<String, Object> adminLogin(AdminLoginRequest req) {
        // Find user by username
        LambdaQueryWrapper<UserDO> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(UserDO::getUsername, req.getUsername())
                   .last("LIMIT 1");
        UserDO user = userMapper.selectOne(userWrapper);
        if (user == null) {
            throw new UserException.UserNotFoundException("用户不存在");
        }

        // Verify password
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new UserException.PasswordErrorException("密码不正确");
        }

        // Check if user is an admin
        LambdaQueryWrapper<SysAdminDO> adminWrapper = new LambdaQueryWrapper<>();
        adminWrapper.eq(SysAdminDO::getUserId, user.getId())
                    .last("LIMIT 1");
        SysAdminDO admin = sysAdminMapper.selectOne(adminWrapper);
        if (admin == null) {
            throw new BusinessException(403, "无管理员权限");
        }

        // Check user status
        if (!"ACTIVE".equals(user.getStatus())) {
            throw new UserException.UserDisabledException("账号已被禁用");
        }

        // Generate JWT token
        String accessToken = jwtTokenProvider.generateAccessToken(
                user.getId(), user.getUsername(), admin.getRole());
        String refreshToken = jwtTokenProvider.generateRefreshToken(
                user.getId(), user.getUsername());

        Map<String, Object> result = new HashMap<>();
        // 前端三端统一使用 token 字段；保留 accessToken 作为兼容字段
        result.put("token", accessToken);
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);
        result.put("expiresIn", jwtTokenProvider.getAccessTokenExpiration() / 1000);
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("nickname", user.getNickname());
        result.put("avatar", user.getAvatar());
        result.put("role", admin.getRole());
        result.put("adminName", admin.getAdminName());
        return result;
    }
}
