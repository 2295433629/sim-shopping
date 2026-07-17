package com.sim.shopping.application.marketing;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sim.shopping.domain.common.exception.BusinessException;
import com.sim.shopping.infrastructure.persistence.entity.SignInRecordDO;
import com.sim.shopping.infrastructure.persistence.entity.UserDO;
import com.sim.shopping.infrastructure.persistence.mapper.SignInRecordMapper;
import com.sim.shopping.infrastructure.persistence.mapper.UserMapper;
import com.sim.shopping.interfaces.dto.common.PageResponse;
import com.sim.shopping.interfaces.dto.signin.SignInRecordResponse;
import com.sim.shopping.interfaces.dto.signin.SignInResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 签到服务，处理用户每日签到、连续签到奖励和签到记录
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Service
public class SignInService {

    private final SignInRecordMapper signInRecordMapper;
    private final UserMapper userMapper;

    private static final int BASE_POINTS = 5;
    private static final int BONUS_3_DAYS = 3;
    private static final int BONUS_7_DAYS = 7;

    public SignInService(SignInRecordMapper signInRecordMapper, UserMapper userMapper) {
        this.signInRecordMapper = signInRecordMapper;
        this.userMapper = userMapper;
    }

    /**
     * 每日签到
     * @param userId userId
     * @return 返回结果
     */
    @Transactional
    public SignInResult signIn(Long userId) {
        LocalDate today = LocalDate.now();

        // Check if already signed in today
        LambdaQueryWrapper<SignInRecordDO> todayWrapper = new LambdaQueryWrapper<>();
        todayWrapper.eq(SignInRecordDO::getUserId, userId)
                .eq(SignInRecordDO::getSignDate, today);
        SignInRecordDO todayRecord = signInRecordMapper.selectOne(todayWrapper);
        if (todayRecord != null) {
            throw new BusinessException(400, "今日已签到");
        }

        // Query yesterday's sign-in record to calculate consecutive days
        LocalDate yesterday = today.minusDays(1);
        LambdaQueryWrapper<SignInRecordDO> yesterdayWrapper = new LambdaQueryWrapper<>();
        yesterdayWrapper.eq(SignInRecordDO::getUserId, userId)
                .eq(SignInRecordDO::getSignDate, yesterday);
        SignInRecordDO yesterdayRecord = signInRecordMapper.selectOne(yesterdayWrapper);

        int consecutiveDays = 1;
        if (yesterdayRecord != null) {
            consecutiveDays = yesterdayRecord.getConsecutiveDays() + 1;
        }

        // Calculate points based on consecutive days
        int earnedPoints = BASE_POINTS;
        if (consecutiveDays > 7) {
            earnedPoints += BONUS_7_DAYS;
        } else if (consecutiveDays > 3) {
            earnedPoints += BONUS_3_DAYS;
        }

        // Create sign-in record
        SignInRecordDO record = new SignInRecordDO();
        record.setUserId(userId);
        record.setSignDate(today);
        record.setConsecutiveDays(consecutiveDays);
        record.setPointsEarned(earnedPoints);
        signInRecordMapper.insert(record);

        // 原子更新用户积分
        userMapper.update(null,
            new LambdaUpdateWrapper<UserDO>()
                .eq(UserDO::getId, userId)
                .setSql("points = COALESCE(points, 0) + " + earnedPoints));

        return new SignInResult(earnedPoints, consecutiveDays, true);
    }

    /**
     * 获取Today Status
     * @param userId userId
     * @return 返回结果
     */
    public SignInResult getTodayStatus(Long userId) {
        LocalDate today = LocalDate.now();

        LambdaQueryWrapper<SignInRecordDO> todayWrapper = new LambdaQueryWrapper<>();
        todayWrapper.eq(SignInRecordDO::getUserId, userId)
                .eq(SignInRecordDO::getSignDate, today);
        SignInRecordDO todayRecord = signInRecordMapper.selectOne(todayWrapper);

        if (todayRecord != null) {
            return new SignInResult(todayRecord.getPointsEarned(), todayRecord.getConsecutiveDays(), true);
        }

        // Get consecutive days from yesterday's record
        int consecutiveDays = 0;
        LocalDate yesterday = today.minusDays(1);
        LambdaQueryWrapper<SignInRecordDO> yesterdayWrapper = new LambdaQueryWrapper<>();
        yesterdayWrapper.eq(SignInRecordDO::getUserId, userId)
                .eq(SignInRecordDO::getSignDate, yesterday);
        SignInRecordDO yesterdayRecord = signInRecordMapper.selectOne(yesterdayWrapper);
        if (yesterdayRecord != null) {
            consecutiveDays = yesterdayRecord.getConsecutiveDays();
        }

        return new SignInResult(0, consecutiveDays, false);
    }

    /**
     * 查询签到记录
     * @param userId userId
     * @param page page
     * @param size size
     * @return 返回结果
     */
    public PageResponse<SignInRecordResponse> getSignInRecords(Long userId, int page, int size) {
        Page<SignInRecordDO> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<SignInRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SignInRecordDO::getUserId, userId)
                .orderByDesc(SignInRecordDO::getSignDate);

        IPage<SignInRecordDO> result = signInRecordMapper.selectPage(pageObj, wrapper);

        List<SignInRecordResponse> list = result.getRecords().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return PageResponse.of(list, result.getTotal(), page, size);
    }

    private SignInRecordResponse convertToResponse(SignInRecordDO record) {
        SignInRecordResponse resp = new SignInRecordResponse();
        resp.setId(record.getId());
        resp.setSignDate(record.getSignDate());
        resp.setConsecutiveDays(record.getConsecutiveDays());
        resp.setPointsEarned(record.getPointsEarned());
        return resp;
    }
}
