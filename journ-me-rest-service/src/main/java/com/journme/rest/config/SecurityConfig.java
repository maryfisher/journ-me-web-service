/*
 * Jumio Inc.
 *
 * Copyright (C) 2010 - 2011
 * All rights reserved.
 */
package com.journme.rest.config;

import com.journme.rest.common.security.AuthTokenService;
import com.journme.rest.common.security.AuthTokenServiceImpl;
import com.journme.rest.common.security.PasswordHashingService;
import com.journme.rest.common.security.PasswordHashingServiceImpl;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Value("${cipherPool.maxTotal:8}")
    private int cipherPoolMaxTotal;

    @Value("${cipherPool.maxIdle:4}")
    private int cipherPoolMaxIdle;

    @Value("${cipherPool.minIdle:4}")
    private int cipherPoolMinIdle;

    @Value("${cipherPool.maxWait:4000}")
    private long cipherPoolMaxWait;

    @Value("${authToken.expirationPeriod:2592000000}")
    private long authTokenExpirationPeriod;

    @Value("${authToken.secretKey:A9xHUK+z2uhz7YknoH7fKcD58R5DfDUvfnIbAuEXZCA=}")
    private String authTokenSecretKey;

    @Bean
    public AuthTokenService authTokenService() throws NoSuchAlgorithmException, InvalidKeyException {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(cipherPoolMaxTotal);
        poolConfig.setMaxIdle(cipherPoolMaxIdle);
        poolConfig.setMinIdle(cipherPoolMinIdle);
        poolConfig.setMaxWaitMillis(cipherPoolMaxWait);

        return new AuthTokenServiceImpl(poolConfig, authTokenExpirationPeriod, authTokenSecretKey);
    }

    @Bean
    public PasswordHashingService passwordHashingService() {
        return new PasswordHashingServiceImpl();
    }
}
