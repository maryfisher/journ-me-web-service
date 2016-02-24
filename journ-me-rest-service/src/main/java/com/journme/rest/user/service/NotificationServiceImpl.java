package com.journme.rest.user.service;

import com.google.common.eventbus.Subscribe;
import com.journme.rest.common.event.MomentEvent;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NotificationServiceImpl implements NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private Map<String, Map<String, SSEChannel>> channels = new ConcurrentHashMap<>();

    public SSEChannel registerSSEChannel(String userId, String authToken) {
        Map<String, SSEChannel> userChannels = channels.get(userId);
        if (userChannels == null) {
            userChannels = new ConcurrentHashMap<>();
            channels.put(userId, userChannels);
        }
        SSEChannel userChannel = userChannels.get(authToken);
        if (userChannel == null) {
            userChannel = new SSEChannel(authToken);
            userChannels.put(authToken, userChannel);
        }
        return userChannel;
    }

    @Subscribe
    public void on(MomentEvent e) {
        String userId = e.getAffectedPerson().getUserId();
        LOGGER.info("Handling event of moment {} for user {}", e.getPayLoad().getId(), userId);

        Map<String, SSEChannel> userChannels = channels.get(userId);
        if (userChannels == null) {
            LOGGER.info("No SSE channels open for user {}", userId);
            return;
        }

        Iterator<Map.Entry<String, SSEChannel>> it = userChannels.entrySet().iterator();
        while (it.hasNext()) {
            SSEChannel channel = it.next().getValue();
            LOGGER.info("Sending SSE stream to user {} via channel {}", userId, channel.getChannelId());
            try {
                final OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder()
                        .mediaType(MediaType.APPLICATION_JSON_TYPE)
                        .name(e.getClass().getSimpleName() + ":" + e.getType())
                        .data(String.class, e.getPayLoad());
                channel.write(eventBuilder.build());
            } catch (IOException e1) {
                LOGGER.info("Exception thrown in channel " + channel.getChannelId(), e1);
                try {
                    channel.close();
                } catch (IOException e2) {
                    LOGGER.warn("Cannot close channel " + channel.getChannelId(), e2);
                } finally {
                    it.remove();
                }
            }
        }

        if (userChannels.isEmpty()) {
            channels.remove(userId);
        }
    }

}
