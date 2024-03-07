package com.cashmanagerbackend.configs;

import com.cashmanagerbackend.configs.properties.RsaKeyProperties;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Component;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {
    private final RsaKeyProperties rsaKeyProperties;

    @Bean
    @Primary
    public JwtDecoder jwtAccessDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaKeyProperties.accessPublicKey()).build();
    }

    @Bean
    @Qualifier("refreshDecoder")
    public JwtDecoder jwtRefreshDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaKeyProperties.refreshPublicKey()).build();
    }

    @Bean
    @Primary
    public JwtEncoder jwtAccessEncoder() {
        JWK jwk = new RSAKey.Builder(rsaKeyProperties.accessPublicKey()).privateKey(rsaKeyProperties.accessPrivateKey()).build();

        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }
    @Bean
    @Qualifier("refreshEncoder")
    public JwtEncoder jwtRefreshEncoder() {
        JWK jwk = new RSAKey.Builder(rsaKeyProperties.refreshPublicKey()).privateKey(rsaKeyProperties.refreshPrivateKey()).build();

        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }
}
