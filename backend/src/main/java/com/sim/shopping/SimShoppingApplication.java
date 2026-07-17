package com.sim.shopping;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * SimShoppingApplication
 *
 * @author Sim Team
 * @since 1.0.0
 */
@SpringBootApplication
@MapperScan("com.sim.shopping.infrastructure.persistence.mapper")
@EnableScheduling
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
