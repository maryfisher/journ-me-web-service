package com.journme.rest.user.resource;

import com.journme.domain.AliasBase;
import com.journme.domain.User;
import com.journme.rest.alias.repository.AliasBaseRepository;
import com.journme.rest.common.errorhandling.JournMeException;
import com.journme.rest.common.security.AuthTokenService;
import com.journme.rest.common.security.IPasswordHashingService;
import com.journme.rest.contract.JournMeExceptionDto;
import com.journme.rest.contract.user.LoginRequest;
import com.journme.rest.contract.user.LoginResponse;
import com.journme.rest.contract.user.RegisterRequest;
import com.journme.rest.user.repository.UserRepository;
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
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * <h1>User (authentication/authorization) endpoints</h1>
 *
 * @author PHT
 * @version 1.0
 * @since 26.10.2015
 */
@Component
@Singleton
@Consumes(MediaType.APPLICATION_JSON_VALUE)
@Produces(MediaType.APPLICATION_JSON_VALUE)
public class UserResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AliasBaseRepository aliasBaseRepository;

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private IPasswordHashingService passwordHashingService;

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
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.put(LoginResponse.AUTH_TOKEN_HEADER_KEY, authTokenService.createAuthToken(user));
            return loginResponse;
        }
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
        aliasBaseRepository.save(newUserFirstAlias);

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

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.put(LoginResponse.AUTH_TOKEN_HEADER_KEY, authTokenService.createAuthToken(newUser));
        return loginResponse;
    }

    private boolean verifyPassword(String loginPassword, User user) {
        String loginPasswordHash = passwordHashingService.stretchAndHashToBase64(
                loginPassword,
                user.getPasswordHashSalt(),
                user.getPasswordHashIterations());
        return user.getPasswordHash().equals(loginPasswordHash);
    }

}

