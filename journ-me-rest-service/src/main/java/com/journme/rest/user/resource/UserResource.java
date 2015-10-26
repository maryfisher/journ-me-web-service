package com.journme.rest.user.resource;

import com.journme.domain.Alias;
import com.journme.domain.User;
import com.journme.rest.alias.repository.AliasRepository;
import com.journme.rest.common.errorhandling.JournMeException;
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
    private AliasRepository aliasRepository;

    @POST
    @Path("/authentication/login")
    public LoginResponse login(LoginRequest loginRequest) {
        LOGGER.info("Incoming request to login user with email {}", loginRequest.getEmail());

        User user = userRepository.findByEmail(loginRequest.getEmail());
        if (user == null) {
            throw new JournMeException("No user exists with email " + loginRequest.getEmail(),
                    Response.Status.UNAUTHORIZED,
                    JournMeExceptionDto.ExceptionCode.AUTHENTICATION_FAILED);
        } else if (!user.getPassword().equals(loginRequest.getPassword())) {
            throw new JournMeException("Wrong password for user with email " + loginRequest.getEmail(),
                    Response.Status.UNAUTHORIZED,
                    JournMeExceptionDto.ExceptionCode.AUTHENTICATION_FAILED);
        } else {
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.getHeaderItems().put(LoginResponse.AUTH_TOKEN_HEADER_KEY, loginRequest.getEmail());
            return loginResponse;
        }
    }

    @POST
    @Path("/authentication/register")
    public LoginResponse register(RegisterRequest registerRequest) {
        LOGGER.info("Incoming request to register user with email {}", registerRequest.getEmail());

        //TODO: move validation to Java Bean Validation framework
        User existingUser = userRepository.findByEmail(registerRequest.getEmail());
        if (existingUser != null) {
            throw new JournMeException("User already exists with email " + registerRequest.getEmail(),
                    Response.Status.BAD_REQUEST,
                    JournMeExceptionDto.ExceptionCode.EMAIL_TAKEN);
        }

        Alias newUserFirstAlias = new Alias();
        newUserFirstAlias.setName(registerRequest.getName());
        aliasRepository.save(newUserFirstAlias);

        User newUser = new User();
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPassword(registerRequest.getPassword());
        newUser.setCurrentAlias(newUserFirstAlias);
        newUser.getAliases().add(newUserFirstAlias);
        userRepository.save(newUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.getHeaderItems().put(LoginResponse.AUTH_TOKEN_HEADER_KEY, "Teddy");
        return loginResponse;
    }

}

