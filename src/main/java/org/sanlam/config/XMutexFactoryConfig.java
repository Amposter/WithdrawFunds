package org.sanlam.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.sanlam.helpers.synchronization.XMutexFactory;

@Configuration
public class XMutexFactoryConfig {

    @Bean
    public XMutexFactory<Integer> intMutexFactory() {
        return new XMutexFactory<Integer>();
    }
}