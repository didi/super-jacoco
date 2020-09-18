package com.xiaoju.basetech.config;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

@Configuration
public class ScheduleConfig implements SchedulingConfigurer {

    private final ThreadFactory threadFactory = new BasicThreadFactory.Builder().namingPattern("scheduled-task-%d").build();

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
    }

    @Bean(destroyMethod = "shutdown")
    public Executor taskExecutor() {
        return new ScheduledThreadPoolExecutor(5, threadFactory);
    }

}