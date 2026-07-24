package com.sim.shopping.application.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.domain.common.exception.UserException;
import com.sim.shopping.infrastructure.persistence.entity.SysAdminDO;
import com.sim.shopping.infrastructure.persistence.entity.UserDO;
import com.sim.shopping.infrastructure.persistence.mapper.SysAdminMapper;
import com.sim.shopping.infrastructure.persistence.mapper.UserMapper;
import com.sim.shopping.infrastructure.common.SystemConstants;
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

        // 确定管理员角色：优先从 sys_admin 表获取，回退到 user 表的 role
        String adminRole = user.getRole();
        String adminName = user.getNickname();
        if (!SystemConstants.ROLE_ADMIN.equalsIgnoreCase(adminRole) && !SystemConstants.ROLE_SUPER_ADMIN.equalsIgnoreCase(adminRole)) {
            // user 表角色不是管理员，尝试查 sys_admin 表
            LambdaQueryWrapper<SysAdminDO> adminWrapper = new LambdaQueryWrapper<>();
            adminWrapper.eq(SysAdminDO::getUserId, user.getId())
                        .last("LIMIT 1");
            SysAdminDO admin = sysAdminMapper.selectOne(adminWrapper);
            if (admin != null) {
                adminRole = admin.getRole();
                adminName = admin.getAdminName();
            } else {
                throw new BusinessException(403, "无管理员权限");
            }
        } else {
            // user 表角色是管理员，尝试补充 adminName
            LambdaQueryWrapper<SysAdminDO> adminWrapper = new LambdaQueryWrapper<>();
            adminWrapper.eq(SysAdminDO::getUserId, user.getId())
                        .last("LIMIT 1");
            SysAdminDO admin = sysAdminMapper.selectOne(adminWrapper);
            if (admin != null) {
                adminRole = admin.getRole();
                adminName = admin.getAdminName();
            }
        }

        // Check user status
        if (!SystemConstants.STATUS_ACTIVE.equals(user.getStatus())) {
            throw new UserException.UserDisabledException("账号已被禁用");
        }

        // Generate JWT token
        String accessToken = jwtTokenProvider.generateAccessToken(
                user.getId(), user.getUsername(), adminRole);
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
        result.put("role", adminRole);
        result.put("adminName", adminName);
        return result;
    }

    /**
     * 修改密码
     * @param username username
     * @param oldPassword oldPassword
     * @param newPassword newPassword
     */
    public void changePassword(String username, String oldPassword, String newPassword) {
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserDO::getUsername, username).last("LIMIT 1");
        UserDO user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException(400, "原密码不正确");
        }
        if (newPassword == null || newPassword.length() < 6) {
            throw new BusinessException(400, "新密码不能少于6位");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userMapper.updateById(user);
    }
}
