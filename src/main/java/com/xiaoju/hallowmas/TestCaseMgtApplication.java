package com.xiaoju.hallowmas;

import com.xiaoju.hallowmas.filter.LogRequestFilter;
import lombok.extern.java.Log;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@EnableEurekaClient
@SpringBootApplication
@MapperScan("com.xiaoju.hallowmas.mapper")
@Log
public class TestCaseMgtApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestCaseMgtApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        LogRequestFilter logRequestFilter = new LogRequestFilter();
        registrationBean.setFilter(logRequestFilter);
        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("/*");
        registrationBean.setUrlPatterns(urlPatterns);
        return registrationBean;
    }
}
