package com.journme.rest.user.service;

import com.journme.domain.User;

import javax.ws.rs.core.UriInfo;
import java.io.IOException;

public class EmailServiceMock implements EmailService {

    @Override
    public void sendForgotPasswordEmail(UriInfo uri, User user) throws IOException {
        // No operation
    }

}
