package com.journme.rest.user.resource;

import com.journme.domain.AliasBase;
import com.journme.domain.User;
import com.journme.domain.repository.AliasBaseRepository;
import com.journme.domain.repository.UserRepository;
import com.journme.rest.common.errorhandling.JournMeException;
import com.journme.rest.common.filter.ProtectedByAuthToken;
import com.journme.rest.common.resource.AbstractResource;
import com.journme.rest.common.security.AuthTokenService;
import com.journme.rest.common.security.PasswordHashingService;
import com.journme.rest.contract.JournMeExceptionDto;
import com.journme.rest.contract.user.LoginRequest;
import com.journme.rest.contract.user.LoginResponse;
import com.journme.rest.contract.user.RegisterRequest;
import com.journme.rest.user.service.EmailService;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;

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
    private PasswordHashingService passwordHashingService;

    @Autowired
    private EmailService emailService;

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
        populatePasswordData(newUser, registerRequest.getPassword());

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
    public void forgotPassword(
            @Context UriInfo uri,
            @NotBlank String email) throws IOException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            LOGGER.info("Incoming request to trigger forgot password workflow for {} ({})", email, user.getId());
            emailService.sendForgotPasswordEmail(uri, user);
        } else {
            LOGGER.info("Incoming request to trigger forgot password workflow not possible for unknown user {}", email);
        }
    }

    @POST
    @Path("/authentication/reset-forgotten-password")
    @ProtectedByAuthToken
    public void resetForgottenPassword(@NotBlank String newPassword) {
        User user = returnUserFromContext();
        LOGGER.info("Incoming request to reset password for user {}", user.getEmail());

        populatePasswordData(user, newPassword);
        userRepository.save(user);
    }

    private LoginResponse sendLoginResponse(User user) {
        LoginResponse loginResponse = new LoginResponse(user);
        loginResponse.put(LoginResponse.AUTH_TOKEN_HEADER_KEY, authTokenService.createAuthToken(user));
        return loginResponse;
    }

    private boolean verifyPassword(String loginPassword, User user) {
        String loginPasswordHash = passwordHashingService.stretchAndHashToBase64(
                loginPassword,
                user.getPasswordHashSalt(),
                user.getPasswordHashIterations());
        return user.getPasswordHash().equals(loginPasswordHash);
    }

    private User populatePasswordData(User user, String password) {
        byte[] salt = passwordHashingService.generateSalt();
        int iterations = PasswordHashingService.RECOMMENDED_ITERATIONS;
        String passwordHashBase64 = passwordHashingService.stretchAndHashToBase64(
                password,
                salt,
                iterations);
        user.setPasswordHash(passwordHashBase64);
        user.setPasswordHashSalt(salt);
        user.setPasswordHashIterations(iterations);
        return user;
    }
}

