package com.example.emergencydashboard.config;

import com.example.emergencydashboard.mapper.IncidentMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public IncidentMapper getIncidentMapper() {
        return IncidentMapper.INSTANCE;
    }
}
