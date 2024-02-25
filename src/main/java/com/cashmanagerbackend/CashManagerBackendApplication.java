package com.cashmanagerbackend;

import com.cashmanagerbackend.configs.RsaKeyConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RsaKeyConfigProperties.class)
@SpringBootApplication
public class CashManagerBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CashManagerBackendApplication.class, args);
	}

}
