package com.cashmanagerbackend.configs.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties("cashmanager.security.rsa")
public record RsaKeyProperties(RSAPublicKey accessPublicKey, RSAPrivateKey accessPrivateKey,
                               RSAPublicKey refreshPublicKey, RSAPrivateKey refreshPrivateKey) {
}