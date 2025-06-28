package com.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class CsvToDbConfig {


    @Bean("csvAsyncExecutor")
    public Executor asyncTaskHandle(){

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(15);
        executor.setMaxPoolSize(100);
        executor.setQueueCapacity(5);
        executor.setThreadNamePrefix("csvthreadname-");
        executor.initialize();
        return executor;
    }



}
