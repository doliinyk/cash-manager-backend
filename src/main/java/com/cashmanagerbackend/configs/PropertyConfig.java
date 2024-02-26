package com.cashmanagerbackend.configs;

import com.cashmanagerbackend.configs.properties.RsaKeyProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(RsaKeyProperties.class)
@Configuration
public class PropertyConfig {
}
