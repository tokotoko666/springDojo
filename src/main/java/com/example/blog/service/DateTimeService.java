package com.example.blog.service;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class DateTimeService {

    @Bean
    public OffsetDateTime now() {
        return OffsetDateTime.now();
    }
}
