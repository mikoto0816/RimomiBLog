package com.rimomi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.rimomi.mapper")
public class RimomiBlogApp {
    public static void main(String[] args) {
        SpringApplication.run(RimomiBlogApp.class, args);
    }
}
