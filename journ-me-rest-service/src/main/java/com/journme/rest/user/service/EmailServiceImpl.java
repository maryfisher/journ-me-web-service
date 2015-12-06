package com.journme.rest.user.service;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.journme.domain.User;
import com.journme.rest.common.security.AuthTokenService;
import com.journme.rest.common.util.Constants;
import com.journme.rest.contract.user.LoginResponse;
import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.net.URL;

public class EmailServiceImpl implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private AuthTokenService authTokenService;

    private final SendGrid sendGrid;

    public EmailServiceImpl(String sendGridUsername, String sendGridPassword) {
        sendGrid = new SendGrid(sendGridUsername, sendGridPassword);
    }

    @Override
    @Async
    public void sendForgotPasswordEmail(UriInfo requestUri, User user) throws IOException {
        UriBuilder passwordForgotUri = requestUri.getBaseUriBuilder()
                .replacePath("")
                .fragment("reset-forgotten-password");
        // Angular needs ? query params to appear after the fragment #, hence concatenate accordingly
        String passwordForgotUrlStr = passwordForgotUri.build().toString();
        passwordForgotUri.replaceQueryParam(LoginResponse.AUTH_TOKEN_HEADER_KEY, authTokenService.createAuthToken(user));
        passwordForgotUrlStr = passwordForgotUrlStr + "?" + passwordForgotUri.build().getQuery();

        URL emailTemplateLocation = Resources.getResource(Constants.Templates.PASSWORD_FORGOT_EMAIL);
        String emailText = Resources.toString(emailTemplateLocation, Charsets.UTF_8);
        emailText = emailText.replace("${passwordForgotUrl}", passwordForgotUrlStr);

        SendGrid.Email sendGridEmail = new SendGrid.Email();
        sendGridEmail.addTo(user.getEmail());
        sendGridEmail.setFrom("support@journ-me.com");
        sendGridEmail.setSubject("Password forgotten");
        sendGridEmail.setText(emailText);

        try {
            SendGrid.Response sendGripdResponse = sendGrid.send(sendGridEmail);
            LOGGER.info("Email sent to {} with success status {}, response code {}, message {}", user.getEmail(), sendGripdResponse.getStatus(), sendGripdResponse.getCode(), sendGripdResponse.getMessage());
        } catch (SendGridException e) {
            LOGGER.warn("Exception when sending email to " + user.getEmail(), e);
        }
    }
}