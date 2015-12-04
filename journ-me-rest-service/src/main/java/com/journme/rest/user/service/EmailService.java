package com.journme.rest.user.service;

import com.journme.domain.User;

import javax.ws.rs.core.UriInfo;
import java.io.IOException;

public interface EmailService {

    void sendForgotPasswordEmail(UriInfo requestUri, User user) throws IOException;

}
