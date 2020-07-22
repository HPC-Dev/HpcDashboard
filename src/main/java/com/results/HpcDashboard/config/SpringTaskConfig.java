package com.results.HpcDashboard.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ComponentScan({ "com.results.HpcDashboard.task" })
public class SpringTaskConfig {

}

