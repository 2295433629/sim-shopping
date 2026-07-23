package com.sim.shopping.infrastructure.aop;

import java.lang.annotation.*;

/**
 * 操作日志注解，标记需要记录操作日志的方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    /**
     * 操作模块
     */
    String module() default "";

    /**
     * 操作类型：新增/修改/删除/查询/导入/导出/审核/登录/登出
     */
    String type() default "";

    /**
     * 操作描述
     */
    String desc() default "";
}
