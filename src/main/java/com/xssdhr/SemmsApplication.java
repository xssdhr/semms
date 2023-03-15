package com.xssdhr;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xssdhr.mapper")
public class SemmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SemmsApplication.class, args);
    }

}
