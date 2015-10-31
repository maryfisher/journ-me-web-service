/*
 * Jumio Inc.
 *
 * Copyright (C) 2010 - 2011
 * All rights reserved.
 */
package com.journme.rest.common.security;

import com.journme.domain.User;

public interface AuthTokenService {

    String createAuthToken(User user);

    User unwrapAuthToken(String authToken);

}
