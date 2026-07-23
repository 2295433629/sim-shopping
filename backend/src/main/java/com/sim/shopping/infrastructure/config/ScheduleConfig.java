package com.sim.shopping.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 定时任务调度配置类，配置 ThreadPoolTaskScheduler 并启用动态调度
 * <p>
 * 使用 Spring 原生 ThreadPoolTaskScheduler + SchedulingConfigurer 实现动态调度，
 * 不引入 Quartz。所有动态任务通过 ScheduleManager 在运行时注册到此调度器。
 *
 * @author Sim Team
 * @since 1.0.0
 */
@Configuration
@EnableScheduling
public class ScheduleConfig implements SchedulingConfigurer {

    /**
     * 定义全局 TaskScheduler Bean，供 Spring 原生 @Scheduled 及动态 ScheduleManager 共用
     *
     * @return ThreadPoolTaskScheduler 实例
     */
    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5);
        scheduler.setThreadNamePrefix("schedule-");
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setAwaitTerminationSeconds(30);
        return scheduler;
    }

    /**
     * 将自定义的 TaskScheduler 设置到 ScheduledTaskRegistrar，
     * 使 Spring 原生 @Scheduled 注解驱动的任务也使用此调度器线程池
     *
     * @param registrar ScheduledTaskRegistrar
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar registrar) {
        registrar.setTaskScheduler(taskScheduler());
    }
}
