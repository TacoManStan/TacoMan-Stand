package com.taco.tacository._to_sort.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanMetadataConfig
{
    @Bean
    public BeanMetadata createBeanMetadata() {
        return new BeanMetadata();
    }
}
