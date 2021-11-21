package com.taco.suit_lady.uncategorized.spring.config;

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
