package com.cashmanagerbackend.configs;

import com.cashmanagerbackend.configs.properties.RsaKeyProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
@EnableJpaAuditing
@EnableConfigurationProperties(RsaKeyProperties.class)
public class ApplicationConfig {
}
