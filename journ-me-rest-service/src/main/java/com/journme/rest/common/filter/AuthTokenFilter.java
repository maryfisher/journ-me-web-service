package com.journme.rest.common.filter;

import com.journme.rest.contract.JournMeExceptionDto;
import com.journme.rest.contract.JournMeExceptionDto.ExceptionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * <h1>Authentication filter</h1>
 * A security filter that is based on auth tokens.
 *
 * @author PHT
 * @version 1.0
 * @since 16.10.2015
 */
@Provider
@ProtectedByAuthToken
@Priority(Priorities.AUTHORIZATION)
public class AuthTokenFilter implements ContainerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String token = requestContext.getHeaders().getFirst("authToken");

        if (!"Teddy".equals(token)) {
            LOGGER.info("Incoming request failed authToken security check");
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).
                    entity(new JournMeExceptionDto(ExceptionCode.AUTH_TOKEN_INVALID)).
                    type("application/json").
                    build());
        }
    }

}
