package com.sim.shopping.infrastructure.aop;

import com.sim.shopping.application.system.OperationLogService;
import com.sim.shopping.infrastructure.common.SystemConstants;
import com.sim.shopping.infrastructure.security.SecurityUtils;
import com.sim.shopping.infrastructure.security.SecurityUser;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

/**
 * 操作日志切面，自动记录带 @Log 注解的方法
 */
@Aspect
@Component
public class OperationLogAspect {

    private final OperationLogService operationLogService;

    public OperationLogAspect(OperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

    @Around("@annotation(com.sim.shopping.infrastructure.aop.Log)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Log logAnnotation = signature.getMethod().getAnnotation(Log.class);

        long start = System.currentTimeMillis();
        Object result = null;
        Exception ex = null;
        try {
            result = point.proceed();
            return result;
        } catch (Exception e) {
            ex = e;
            throw e;
        } finally {
            long cost = System.currentTimeMillis() - start;
            saveLog(logAnnotation, point, cost, ex);
        }
    }

    private void saveLog(Log logAnnotation, ProceedingJoinPoint point, long cost, Exception ex) {
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs == null) return;

            HttpServletRequest request = attrs.getRequest();
            String method = request.getMethod();
            String url = request.getRequestURI();
            String ip = request.getRemoteAddr();

            Long operatorId = null;
            String operatorName = "anonymous";
            String operatorType = "UNKNOWN";
            try {
                SecurityUser user = SecurityUtils.getCurrentUser();
                operatorId = user.getUserId();
                operatorName = user.getUsername();
                operatorType = user.getUserType();
            } catch (Exception e) {
                // 未登录场景（如登录接口本身），无需记录操作人
            }

            String module = logAnnotation.module();
            String type = logAnnotation.type();
            if (module.isEmpty()) {
                module = inferModule(url);
            }
            if (type.isEmpty()) {
                type = inferType(method);
            }

            String params = truncate(toJson(point.getArgs()), SystemConstants.LOG_TRUNCATE_LENGTH);
            String response = ex != null ? ex.getMessage() : "success";
            response = truncate(response, SystemConstants.LOG_TRUNCATE_LENGTH);

            operationLogService.saveOperationLog(
                    operatorId, operatorName, operatorType,
                    module, type, method, url, params, response, cost, ip
            );
        } catch (Exception e) {
            // 日志记录失败不应影响主业务，静默处理避免级联故障
        }
    }

    private String inferModule(String url) {
        if (url.contains("/user")) return "用户管理";
        if (url.contains("/merchant")) return "商家管理";
        if (url.contains("/product")) return "商品管理";
        if (url.contains("/order")) return "订单管理";
        if (url.contains("/category")) return "分类管理";
        if (url.contains("/brand")) return "品牌管理";
        if (url.contains("/coupon")) return "优惠券管理";
        if (url.contains("/flash")) return "秒杀管理";
        if (url.contains("/activity")) return "活动管理";
        if (url.contains("/banner")) return "Banner管理";
        if (url.contains("/review")) return "评价管理";
        if (url.contains("/role")) return "角色管理";
        if (url.contains("/permission")) return "权限管理";
        if (url.contains("/menu")) return "菜单管理";
        if (url.contains("/dict")) return "字典管理";
        if (url.contains("/schedule")) return "定时任务";
        if (url.contains("/point")) return "积分管理";
        if (url.contains("/login") || url.contains("/logout")) return "认证";
        return "系统";
    }

    private String inferType(String method) {
        return switch (method.toUpperCase()) {
            case "POST" -> "新增";
            case "PUT" -> "修改";
            case "DELETE" -> "删除";
            case "GET" -> "查询";
            default -> "操作";
        };
    }

    private String toJson(Object[] args) {
        if (args == null || args.length == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (Object arg : args) {
            if (arg == null) continue;
            String name = arg.getClass().getSimpleName();
            if (name.contains("Request") || name.contains("DTO") || name.contains("Map")) {
                sb.append(name).append(", ");
            }
        }
        return sb.toString().replaceAll(", $", "");
    }

    private String truncate(String s, int max) {
        if (s == null) return "";
        return s.length() > max ? s.substring(0, max) + "..." : s;
    }
}
