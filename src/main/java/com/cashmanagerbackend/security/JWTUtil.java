package com.cashmanagerbackend.security;


import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;
import com.auth0.jwt.JWT;


@Component
public class JWTUtil {
    @Value("${JWT_SECRET}")
    private String secret;

    public String generateToken(String name){
        Date date = Date.from(ZonedDateTime.now().plusDays(10).toInstant());

        return JWT.create()
                .withSubject("User details")
                .withClaim("login",  name)
                .withIssuedAt(new Date())
                .withIssuer("REST-Timetable")
                .withExpiresAt(date)
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTakenAndRetrieveClaim(String token) throws JWTVerificationException {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User details")
                .withIssuer("REST-Timetable")
                .build();

        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return decodedJWT.getClaim("login").asString();
    }
}
