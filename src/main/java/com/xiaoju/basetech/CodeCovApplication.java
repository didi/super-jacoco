package com.xiaoju.basetech;

import lombok.extern.java.Log;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableAsync
@EnableScheduling
@SpringBootApplication
@Log
@EnableSwagger2
@MapperScan("com.xiaoju.basetech.dao")
public class CodeCovApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeCovApplication.class, args);
    }

}
