/*
 * Jumio Inc.
 *
 * Copyright (C) 2010 - 2011
 * All rights reserved.
 */
package com.journme.rest.common.security;

public interface PasswordHashingService {

    int RECOMMENDED_ITERATIONS = 1000;

    String stretchAndHashToBase64(String password, byte[] salt, int iterations);

    byte[] generateSalt();

}
