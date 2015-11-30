package com.journme.rest.user.resource;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.journme.domain.AliasBase;
import com.journme.domain.User;
import com.journme.domain.repository.AliasBaseRepository;
import com.journme.domain.repository.UserRepository;
import com.journme.rest.common.errorhandling.JournMeException;
import com.journme.rest.common.filter.ProtectedByAuthToken;
import com.journme.rest.common.resource.AbstractResource;
import com.journme.rest.common.security.AuthTokenService;
import com.journme.rest.common.security.IPasswordHashingService;
import com.journme.rest.common.util.Constants;
import com.journme.rest.contract.JournMeExceptionDto;
import com.journme.rest.contract.user.LoginRequest;
import com.journme.rest.contract.user.LoginResponse;
import com.journme.rest.contract.user.RegisterRequest;
import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

/**
 * <h1>User (authentication/authorization) endpoints</h1>
 *
 * @author PHT
 * @version 1.0
 * @since 26.10.2015
 */
@Component
@Singleton
public class UserResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AliasBaseRepository aliasBaseRepository;

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private IPasswordHashingService passwordHashingService;

    @Autowired
    private SendGrid sendGrid;

    @POST
    @Path("/authentication/login")
    public LoginResponse login(@NotNull @Valid LoginRequest loginRequest) {
        LOGGER.info("Incoming request to login user with email {}", loginRequest.getEmail());

        User user = userRepository.findByEmail(loginRequest.getEmail());
        if (user == null) {
            throw new JournMeException("No user exists with email " + loginRequest.getEmail(),
                    Response.Status.UNAUTHORIZED,
                    JournMeExceptionDto.ExceptionCode.AUTHENTICATION_FAILED);
        } else if (!verifyPassword(loginRequest.getPassword(), user)) {
            throw new JournMeException("Wrong password for user with email " + loginRequest.getEmail(),
                    Response.Status.UNAUTHORIZED,
                    JournMeExceptionDto.ExceptionCode.AUTHENTICATION_FAILED);
        } else {
            return sendLoginResponse(user);
        }
    }

    @POST
    @Path("/authentication/token-login")
    @ProtectedByAuthToken
    public LoginResponse tokenLogin() {
        User loggedInUser = returnUserFromContext();
        LOGGER.info("Automatic token login for user with email {}", loggedInUser.getEmail());
        return sendLoginResponse(loggedInUser);
    }

    private LoginResponse sendLoginResponse(User user) {
        LoginResponse loginResponse = new LoginResponse(user);
        loginResponse.put(LoginResponse.AUTH_TOKEN_HEADER_KEY, authTokenService.createAuthToken(user));
        return loginResponse;
    }

    @POST
    @Path("/authentication/register")
    public LoginResponse register(@NotNull @Valid RegisterRequest registerRequest) {
        LOGGER.info("Incoming request to register user with email {}", registerRequest.getEmail());

        User existingUser = userRepository.findByEmail(registerRequest.getEmail());
        if (existingUser != null) {
            throw new JournMeException("User already exists with email " + registerRequest.getEmail(),
                    Response.Status.BAD_REQUEST,
                    JournMeExceptionDto.ExceptionCode.EMAIL_TAKEN);
        }

        AliasBase newUserFirstAlias = new AliasBase();
        newUserFirstAlias.setName(registerRequest.getName());
        newUserFirstAlias = aliasBaseRepository.save(newUserFirstAlias);

        User newUser = new User();
        newUser.setEmail(registerRequest.getEmail());
        newUser.setCurrentAlias(newUserFirstAlias);
        newUser.getAliases().add(newUserFirstAlias);
        byte[] salt = passwordHashingService.generateSalt();
        int iterations = IPasswordHashingService.RECOMMENDED_ITERATIONS;
        String passwordHashBase64 = passwordHashingService.stretchAndHashToBase64(
                registerRequest.getPassword(),
                salt,
                iterations);
        newUser.setPasswordHash(passwordHashBase64);
        newUser.setPasswordHashSalt(salt);
        newUser.setPasswordHashIterations(iterations);

        newUser = userRepository.save(newUser);

        LoginResponse loginResponse = new LoginResponse(newUser);
        loginResponse.put(LoginResponse.AUTH_TOKEN_HEADER_KEY, authTokenService.createAuthToken(newUser));
        return loginResponse;
    }

    @POST
    @Path("/authentication/logout")
    @ProtectedByAuthToken
    public void logout() {
        User user = returnUserFromContext();
        LOGGER.info("Incoming request to logout user with id {}", user.getId());
    }

    @POST
    @Path("/authentication/forgot-password")
    @Consumes(MediaType.TEXT_PLAIN_VALUE)
    public void forgotPassword(
            @Context UriInfo uri,
            @NotBlank String email) throws IOException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            LOGGER.info("Incoming request to trigger forgot password workflow for {} ({})", email, user.getId());

            URL emailTemplateUrl = Resources.getResource(Constants.Templates.PASSWORD_FORGOT_EMAIL);
            String emailText = Resources.toString(emailTemplateUrl, Charsets.UTF_8);
            URI passwordForgotUrl = uri.getBaseUriBuilder()
                    .replacePath("")
                    .fragment("forgot-password")
                    .queryParam(LoginResponse.AUTH_TOKEN_HEADER_KEY, authTokenService.createAuthToken(user))
                    .build();
            emailText = emailText.replace("${passwordForgotUrl}", passwordForgotUrl.toString());

            SendGrid.Email sendGridEmail = new SendGrid.Email();
            sendGridEmail.addTo(email);
            sendGridEmail.setFrom("support@journ-me.com");
            sendGridEmail.setSubject("Password forgotten");
            sendGridEmail.setText(emailText);

            try {
                SendGrid.Response sendGripdResponse = sendGrid.send(sendGridEmail);
                LOGGER.info("Email sent to {} with success status {}, response code {}, message {}", email, sendGripdResponse.getStatus(), sendGripdResponse.getCode(), sendGripdResponse.getMessage());
            } catch (SendGridException e) {
                LOGGER.warn("Failure to send email to " + email, e);
            }
        } else {
            LOGGER.info("Incoming request to trigger password reset not possible for unknown user {}", email);
        }
    }

    @POST
    @Path("/authentication/reset-password")
    @Consumes(MediaType.TEXT_PLAIN_VALUE)
    @ProtectedByAuthToken
    public void resetPassword(@NotBlank String newPassword) {
        User user = returnUserFromContext();
        LOGGER.info("Incoming request to reset password for user {}", user.getEmail());

        byte[] salt = passwordHashingService.generateSalt();
        int iterations = IPasswordHashingService.RECOMMENDED_ITERATIONS;
        String passwordHashBase64 = passwordHashingService.stretchAndHashToBase64(
                newPassword,
                salt,
                iterations);
        user.setPasswordHash(passwordHashBase64);
        user.setPasswordHashSalt(salt);
        user.setPasswordHashIterations(iterations);
        userRepository.save(user);
    }

    private boolean verifyPassword(String loginPassword, User user) {
        String loginPasswordHash = passwordHashingService.stretchAndHashToBase64(
                loginPassword,
                user.getPasswordHashSalt(),
                user.getPasswordHashIterations());
        return user.getPasswordHash().equals(loginPasswordHash);
    }

}

