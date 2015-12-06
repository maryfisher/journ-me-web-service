package com.journme.rest.user.service;

import com.google.common.eventbus.Subscribe;
import com.journme.rest.common.event.Event;
import com.journme.rest.common.event.MomentEvent;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class NotificationServiceImpl implements NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private Map<String, Set<SSEChannel>> channels = new ConcurrentHashMap<>();

    public boolean registerSSEChannel(String userId, SSEChannel sseChannel) {
        if (!channels.containsKey(userId)) {
            channels.put(userId, ConcurrentHashMap.newKeySet());
        }
        return channels.get(userId).add(sseChannel);
    }

    @Subscribe
    public void on(MomentEvent e) {
        LOGGER.info("Handling MomentEvent {}", e.getUserId());

        Set<SSEChannel> userChannels = channels.get(e.getUserId());
        if (userChannels == null) {
            LOGGER.info("No user channels open dor user {}", e.getUserId());
            return;
        }

        Iterator<SSEChannel> it = userChannels.iterator();
        while (it.hasNext()) {
            SSEChannel channel = it.next();
            LOGGER.info("Sending SSE stream via to channel {}", channel.getChannelId());
            try {
                final OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
                eventBuilder.name(e.getType().toString());
                eventBuilder.data(String.class, e.getUserId());
                channel.write(eventBuilder.build());
            } catch (IOException e1) {
                LOGGER.info("Channel {} is dead", channel.getChannelId());
                it.remove();
            }
        }

        if (userChannels.isEmpty()) {
            channels.remove(e.getUserId());
        }
    }


    @Subscribe
    public void on(Event e) {
        LOGGER.info("Handling Event");
    }
}
