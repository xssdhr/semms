package com.xssdhr.semms;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xssdhr.semms.mapper")
public class SemmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SemmsApplication.class, args);
	}

}
