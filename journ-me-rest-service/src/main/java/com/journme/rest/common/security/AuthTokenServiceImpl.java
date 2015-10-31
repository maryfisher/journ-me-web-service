/*
 * Jumio Inc.
 *
 * Copyright (C) 2010 - 2011
 * All rights reserved.
 */
package com.journme.rest.common.security;

import com.journme.domain.User;
import com.journme.rest.common.errorhandling.JournMeException;
import com.journme.rest.contract.JournMeExceptionDto.ExceptionCode;
import com.journme.rest.user.repository.UserRepository;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.NoSuchElementException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.core.Response;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public class AuthTokenServiceImpl implements AuthTokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthTokenServiceImpl.class);

    private static final String SEPARATOR_CHARACTER = ";";

    private final long authTokenExpirationPeriod;

    private final byte[] secretKeyForAuthToken;

    private final GenericObjectPool<Cipher> cipherPool;

    @Autowired
    private UserRepository userRepository;

    public AuthTokenServiceImpl(
            GenericObjectPoolConfig poolConfig,
            long authTokenExpirationPeriod,
            String secretKeyBase64) throws NoSuchAlgorithmException, InvalidKeyException {
        LOGGER.info("Instantiating cipherPool with configuration: maxTotal={}, maxIdle={}, minIdle={}, maxWait={}",
                poolConfig.getMaxTotal(), poolConfig.getMaxIdle(), poolConfig.getMinIdle(), poolConfig.getMaxWaitMillis());
        this.cipherPool = new GenericObjectPool<>(new AesCbcCipherFactory(), poolConfig);

        this.authTokenExpirationPeriod = authTokenExpirationPeriod;
        secretKeyForAuthToken = Base64.decodeBase64(secretKeyBase64);
        if ((secretKeyForAuthToken.length * Byte.SIZE) != AesCbcCipherFactory.KEY_BIT_SIZE) {
            throw new InvalidKeyException("Secret key must have bit length of " + AesCbcCipherFactory.KEY_BIT_SIZE);
        }
    }

    @Override
    public String createAuthToken(User user) {
        LOGGER.info("Creating auth token for user {}", user.getId());
        String messageToEncrypt = new Date().getTime() +
                SEPARATOR_CHARACTER  + user.getId() +
                SEPARATOR_CHARACTER + user.getPasswordHash();
        byte[] encryptedAuthToken = encryptMessage(messageToEncrypt, this.secretKeyForAuthToken);
        return Base64.encodeBase64URLSafeString(encryptedAuthToken);
    }

    @Override
    public User unwrapAuthToken(String authToken) {
        if (StringUtils.isEmpty(authToken)) {
            LOGGER.info("AuthToken value is empty");
            return null;
        }

        byte[] tokenToDecrypt = Base64.decodeBase64(authToken);
        String decryptedMessage = decryptMessage(tokenToDecrypt, this.secretKeyForAuthToken);
        String[] tokenElements = decryptedMessage.split(SEPARATOR_CHARACTER);

        try {
            long timestamp = Long.parseLong(tokenElements[0]);
            if (timestamp + authTokenExpirationPeriod < new Date().getTime()) {
                LOGGER.info("AuthToken contains timestamp older than {} days", authTokenExpirationPeriod / 86400000L);
                return null;
            }

            String userId = tokenElements[1];
            String passwordHash = tokenElements[2];
            User user = userRepository.findOne(userId);
            if (!user.getPasswordHash().equals(passwordHash)) {
                LOGGER.info("Password hash has changed in the meantime");
                return null;
            }

            return user;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException | NullPointerException e) {
            LOGGER.info("Decrypted auth token has invalid elements");
            return null;
        }
    }

    private byte[] encryptMessage(String message, byte[] secretKey) {
        Cipher cipher = null;
        try {
            // 1) Initialize cipher
            cipher = cipherPool.borrowObject();
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, AesCbcCipherFactory.KEYGEN_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            // 2) Generate IV (cipher will generate IV if it is required but was not provided)
            byte[] iv = cipher.getIV();

            // 3) Encrypt message using AES encryption
            byte[] messageBytes = message.getBytes(AesCbcCipherFactory.CHARSET);
            byte[] result = new byte[cipher.getOutputSize(messageBytes.length) + iv.length];
            int encryptedLength = cipher.doFinal(messageBytes, 0, messageBytes.length, result, 0);

            // 4) Append IV to encryption result
            System.arraycopy(iv, 0, result, encryptedLength, iv.length);

            return result;
        } catch (NoSuchElementException e) {
            throw new JournMeException("Cipher pool exhausted",
                    Response.Status.SERVICE_UNAVAILABLE,
                    ExceptionCode.POOL_EXHAUSTED,
                    e);
        } catch (Exception e) {
            throw new JournMeException("Encryption failure",
                    Response.Status.INTERNAL_SERVER_ERROR,
                    ExceptionCode.INTERNAL_SYSTEM_PROBLEM,
                    e);
        } finally {
            if (cipher != null) {
                try {
                    cipherPool.returnObject(cipher);
                } catch (Exception e) {
                    LOGGER.warn("Unable to return cipher to cipherPool at end of encryption", e);
                }
            }
        }
    }

    private String decryptMessage(byte[] message, byte[] secretKey) {
        Cipher cipher = null;
        try {
            cipher = cipherPool.borrowObject();
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, AesCbcCipherFactory.KEYGEN_ALGORITHM);

            int ivLength = cipher.getBlockSize();
            IvParameterSpec ivParameterSpec = new IvParameterSpec(message, message.length - ivLength, ivLength);

            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] decrypted = cipher.doFinal(message, 0, message.length - ivLength);

            return new String(decrypted, 0, decrypted.length, AesCbcCipherFactory.CHARSET);
        } catch (NoSuchElementException e) {
            throw new JournMeException("Cipher pool exhausted",
                    Response.Status.SERVICE_UNAVAILABLE,
                    ExceptionCode.POOL_EXHAUSTED,
                    e);
        } catch (Exception e) {
            throw new JournMeException("Decryption failure",
                    Response.Status.INTERNAL_SERVER_ERROR,
                    ExceptionCode.INTERNAL_SYSTEM_PROBLEM,
                    e);
        } finally {
            if (cipher != null) {
                try {
                    cipherPool.returnObject(cipher);
                } catch (Exception e) {
                    LOGGER.warn("Unable to return cipher to cipherPool at end of decryption", e);
                }
            }
        }
    }
}
