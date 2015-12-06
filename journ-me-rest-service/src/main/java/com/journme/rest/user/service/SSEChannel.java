package com.journme.rest.user.service;

import org.glassfish.jersey.media.sse.EventOutput;

/**
 * The SSE output channel with a unique channel ID
 * <p>
 * Created by PHT on 21.02.2016.
 */
public class SSEChannel extends EventOutput {

    private final String channelId;

    public SSEChannel(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelId() {
        return channelId;
    }

    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof SSEChannel && this.channelId.equals(((SSEChannel) other).channelId);

    }
}
