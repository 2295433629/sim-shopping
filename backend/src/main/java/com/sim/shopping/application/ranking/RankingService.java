package com.sim.shopping.application.ranking;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sim.shopping.infrastructure.persistence.entity.OrderDO;
import com.sim.shopping.infrastructure.persistence.entity.SignInRecordDO;
import com.sim.shopping.infrastructure.persistence.entity.UserDO;
import com.sim.shopping.infrastructure.persistence.mapper.OrderMapper;
import com.sim.shopping.infrastructure.persistence.mapper.SignInRecordMapper;
import com.sim.shopping.infrastructure.persistence.mapper.UserMapper;
import com.sim.shopping.interfaces.dto.ranking.RankingItemResponse;
import com.sim.shopping.interfaces.dto.ranking.RankingListResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 排行榜服务，处理各类排行数据的统计和查询
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Service
public class RankingService {

    private final OrderMapper orderMapper;
    private final SignInRecordMapper signInRecordMapper;
    private final UserMapper userMapper;

    private static final List<String> VALID_ORDER_STATUSES = Arrays.asList("PAID", "COMPLETED");
    private static final String ANONYMOUS_NICKNAME = "匿名用户";
    private static final int TOP_LIMIT = 20;
    private static final String UNIT_YUAN = "元";
    private static final String UNIT_TIMES = "次";

    public RankingService(OrderMapper orderMapper, SignInRecordMapper signInRecordMapper, UserMapper userMapper) {
        this.orderMapper = orderMapper;
        this.signInRecordMapper = signInRecordMapper;
        this.userMapper = userMapper;
    }

    /**
     * 查询消费排行榜
     * @param period period
     * @return 返回结果
     */
    public RankingListResponse getConsumptionRanking(String period) {
        LambdaQueryWrapper<OrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(OrderDO::getStatus, VALID_ORDER_STATUSES);

        applyOrderPeriod(wrapper, period);

        List<OrderDO> orders = orderMapper.selectList(wrapper);

        Map<Long, BigDecimal> amountMap = orders.stream()
                .filter(o -> o.getUserId() != null && o.getTotalAmount() != null)
                .collect(Collectors.groupingBy(
                        OrderDO::getUserId,
                        Collectors.reducing(BigDecimal.ZERO, OrderDO::getTotalAmount, BigDecimal::add)
                ));

        List<Map.Entry<Long, BigDecimal>> sorted = amountMap.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(TOP_LIMIT)
                .collect(Collectors.toList());

        Map<Long, UserDO> userMap = fetchUserMap(sorted.stream().map(Map.Entry::getKey).collect(Collectors.toList()));

        List<RankingItemResponse> list = buildRankingList(sorted, userMap, UNIT_YUAN, (entry) -> entry.getValue());

        RankingListResponse response = new RankingListResponse();
        response.setPeriod(period);
        response.setList(list);
        return response;
    }

    /**
     * 查询签到排行榜
     * @param period period
     * @return 返回结果
     */
    public RankingListResponse getSignInRanking(String period) {
        LambdaQueryWrapper<SignInRecordDO> wrapper = new LambdaQueryWrapper<>();

        applySignInPeriod(wrapper, period);

        List<SignInRecordDO> records = signInRecordMapper.selectList(wrapper);

        Map<Long, Long> countMap = records.stream()
                .filter(r -> r.getUserId() != null)
                .collect(Collectors.groupingBy(
                        SignInRecordDO::getUserId,
                        Collectors.counting()
                ));

        List<Map.Entry<Long, Long>> sorted = countMap.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .limit(TOP_LIMIT)
                .collect(Collectors.toList());

        Map<Long, UserDO> userMap = fetchUserMap(sorted.stream().map(Map.Entry::getKey).collect(Collectors.toList()));

        List<RankingItemResponse> list = buildRankingList(sorted, userMap, UNIT_TIMES, (entry) -> BigDecimal.valueOf(entry.getValue()));

        RankingListResponse response = new RankingListResponse();
        response.setPeriod(period);
        response.setList(list);
        return response;
    }

    private void applyOrderPeriod(LambdaQueryWrapper<OrderDO> wrapper, String period) {
        if (period == null) {
            return;
        }
        LocalDate now = LocalDate.now();
        switch (period.toUpperCase()) {
            case "WEEK":
                LocalDateTime weekStart = now.with(DayOfWeek.MONDAY).atStartOfDay();
                LocalDateTime weekEnd = now.with(DayOfWeek.SUNDAY).atTime(LocalTime.MAX);
                wrapper.ge(OrderDO::getCreatedAt, weekStart)
                        .le(OrderDO::getCreatedAt, weekEnd);
                break;
            case "MONTH":
                LocalDateTime monthStart = now.withDayOfMonth(1).atStartOfDay();
                LocalDateTime monthEnd = now.withDayOfMonth(now.lengthOfMonth()).atTime(LocalTime.MAX);
                wrapper.ge(OrderDO::getCreatedAt, monthStart)
                        .le(OrderDO::getCreatedAt, monthEnd);
                break;
            case "ALL":
            default:
                break;
        }
    }

    private void applySignInPeriod(LambdaQueryWrapper<SignInRecordDO> wrapper, String period) {
        if (period == null) {
            return;
        }
        LocalDate now = LocalDate.now();
        switch (period.toUpperCase()) {
            case "WEEK":
                wrapper.ge(SignInRecordDO::getSignDate, now.with(DayOfWeek.MONDAY))
                        .le(SignInRecordDO::getSignDate, now.with(DayOfWeek.SUNDAY));
                break;
            case "MONTH":
                wrapper.ge(SignInRecordDO::getSignDate, now.withDayOfMonth(1))
                        .le(SignInRecordDO::getSignDate, now.withDayOfMonth(now.lengthOfMonth()));
                break;
            case "ALL":
            default:
                break;
        }
    }

    private Map<Long, UserDO> fetchUserMap(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return java.util.Collections.emptyMap();
        }
        return userMapper.selectBatchIds(userIds).stream()
                .collect(Collectors.toMap(UserDO::getId, Function.identity(), (u1, u2) -> u1));
    }

    private <T> List<RankingItemResponse> buildRankingList(List<Map.Entry<Long, T>> sortedEntries,
                                                            Map<Long, UserDO> userMap,
                                                            String unit,
                                                            Function<Map.Entry<Long, T>, BigDecimal> valueExtractor) {
        List<RankingItemResponse> list = new ArrayList<>();
        int rank = 1;
        for (Map.Entry<Long, T> entry : sortedEntries) {
            Long userId = entry.getKey();
            UserDO user = userMap.get(userId);

            RankingItemResponse item = new RankingItemResponse();
            item.setRank(rank++);
            item.setUserId(userId);
            item.setNickname(user != null && user.getNickname() != null ? user.getNickname() : ANONYMOUS_NICKNAME);
            item.setAvatar(user != null ? user.getAvatar() : null);
            item.setValue(valueExtractor.apply(entry));
            item.setUnit(unit);
            list.add(item);
        }
        return list;
    }
}
