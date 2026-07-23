package com.sim.shopping;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * SimShoppingApplication
 * <p>
 * 注：@EnableScheduling 已移至 {@link com.sim.shopping.infrastructure.config.ScheduleConfig}，
 * 与 ThreadPoolTaskScheduler + SchedulingConfigurer 统一管理，避免重复声明。
 *
 * @author Sim Team
 * @since 1.0.0
 */
@SpringBootApplication
@MapperScan("com.sim.shopping.infrastructure.persistence.mapper")
public class SimShoppingApplication extends SpringBootServletInitializer {

    /**
     * 程序入口
     * @param args args
     */
    public static void main(String[] args) {
        SpringApplication.run(SimShoppingApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SimShoppingApplication.class);
    }
}
