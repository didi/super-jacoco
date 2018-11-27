package com.xiaoju.hallowmas.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yehonggang on 17/5/24.
 */
@Component
public class ApplicationConfig {
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        LogRequestFilter actionFilter = new LogRequestFilter();
        registrationBean.setFilter(actionFilter);
        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("/service/extract/json/*");
        registrationBean.setUrlPatterns(urlPatterns);
        return registrationBean;
    }
}
