/*
 * Jumio Inc.
 *
 * Copyright (C) 2010 - 2011
 * All rights reserved.
 */
package com.journme.rest.common.security;

import com.journme.rest.common.errorhandling.JournMeException;
import com.journme.rest.contract.JournMeExceptionDto;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.ws.rs.core.Response;
import org.apache.commons.codec.binary.Base64;

public class PasswordHashingServiceImpl implements IPasswordHashingService {

    public static final String HASH_ALGORITHM = "PBKDF2WithHmacSHA512";

    public static final int KEY_LENGTH = 512;

    public static final int SALT_LENGTH = 16;

    @Override
    public String stretchAndHashToBase64(String password, byte[] salt, int iterations) {
        char[] chars = password.toCharArray();
        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, KEY_LENGTH);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance(HASH_ALGORITHM);
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return Base64.encodeBase64URLSafeString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new JournMeException("Password hashing failed",
                    Response.Status.INTERNAL_SERVER_ERROR,
                    JournMeExceptionDto.ExceptionCode.INTERNAL_SYSTEM_PROBLEM,
                    e);
        }
    }

    @Override
    public byte[] generateSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);
        return salt;
    }

}
