package com.journme.rest.common.filter;

import com.journme.domain.Alias;
import com.journme.domain.User;
import com.journme.rest.common.security.AuthTokenService;
import com.journme.rest.contract.JournMeExceptionDto;
import com.journme.rest.contract.JournMeExceptionDto.ExceptionCode;
import com.journme.rest.contract.user.LoginResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Autowired
    private AuthTokenService authTokenService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authToken = requestContext.getHeaders().getFirst(LoginResponse.AUTH_TOKEN_HEADER_KEY);

        User user = authTokenService.unwrapAuthToken(authToken);
        if (user != null) {
            requestContext.setSecurityContext(new JMSecurityContext(requestContext.getSecurityContext(), user));
        } else {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).
                    entity(new JournMeExceptionDto(ExceptionCode.AUTH_TOKEN_INVALID)).
                    type("application/json").
                    build());
        }
    }

    public static User returnUserFromContext(SecurityContext sc) {
        return ((UserPrincipal)sc.getUserPrincipal()).user;
    }

    // theAlias can either be an instance of Alias or of String
    public static Alias returnAliasFromContext(SecurityContext sc, String theAliasId) {
        List<Alias> userAliases = ((UserPrincipal)sc.getUserPrincipal()).user.getAliases();

        for (Alias userAlias : userAliases) {
            if (userAlias.equalsId(theAliasId)) {
                return userAlias;
            }
        }
        return null;
    }

    private static class JMSecurityContext implements SecurityContext {

        private final SecurityContext originalSc;

        private final UserPrincipal principal;

        public JMSecurityContext(SecurityContext sc, User authenticatedUser) {
            originalSc = sc;
            principal = new UserPrincipal(authenticatedUser);
        }

        @Override
        public Principal getUserPrincipal() {
            return principal;
        }

        @Override
        public boolean isUserInRole(String role) {
            return true;
        }

        @Override
        public boolean isSecure() {
            return originalSc != null && originalSc.isSecure();
        }

        @Override
        public String getAuthenticationScheme() {
            return ProtectedByAuthToken.TOKEN_AUTH;
        }
    }

    private static class UserPrincipal implements Principal {

        private final User user;

        public UserPrincipal(User user) {
            if (user == null || user.getId() == null) {
                throw new IllegalArgumentException("Cannot set principal with underlying user/userId as NULL");
            } else {
                this.user = user;
            }
        }

        @Override
        public boolean equals(Object other) {
            return (this == other) ||
                    (other instanceof Principal) && getName().equals(((Principal)other).getName());
        }

        @Override
        public int hashCode() {
            return getName().hashCode();
        }

        @Override
        public String getName() {
            return user.getId();
        }
    }

}
