package com.journme.rest.user.resource;

import com.journme.rest.common.filter.ProtectedByAuthToken;
import com.journme.rest.common.resource.AbstractResource;
import com.journme.rest.contract.user.LoginResponse;
import com.journme.rest.user.service.NotificationService;
import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.SseFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Produces;

@Component
@Singleton
public class NotificationResource extends AbstractResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationResource.class);

    @Autowired
    private NotificationService notificationService;

    @GET
    @Produces(SseFeature.SERVER_SENT_EVENTS)
    @ProtectedByAuthToken
    public EventOutput subscribeToNotifications(@HeaderParam(LoginResponse.AUTH_TOKEN_HEADER_KEY) String authToken) {
        String userId = returnUserFromContext().getId();
        LOGGER.info("Incoming request to establish SSE connection for use {}", userId);
        return notificationService.registerSSEChannel(userId, authToken);
    }

}
