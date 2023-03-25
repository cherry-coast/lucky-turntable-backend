package com.cherry.lucky;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author cherry
 */
@SpringBootApplication
//@ComponentScan(basePackages = {"com.cherry.lucky.*"})
@MapperScan(basePackages = { "com.cherry.lucky.mapper" })
public class LuckyTurntableBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(LuckyTurntableBackendApplication.class, args);
    }

}
