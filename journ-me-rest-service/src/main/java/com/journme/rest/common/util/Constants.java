package com.journme.rest.common.util;

import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class Constants {

    public static final long TWO_WEEKS_MILLISEC = 2 * 14 * 24 * 60 * 60 * 1000L;

    public static final int THUMBNAIL_SIZE = 80;

    public static final Set<MediaType> SUPPORTED_MEDIA_TYPE = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(
                    MediaType.IMAGE_JPEG,
                    MediaType.IMAGE_PNG,
                    MediaType.IMAGE_GIF
            ))
    );

    public static final String WORD_SEPARATOR_CHARACTER = " ";

    private Constants() {
    }

    public static abstract class Templates {
        public static final String JM_CONFIG_FILE = "templates/jm-config.tpl.js";
        public static final String PASSWORD_FORGOT_EMAIL = "templates/password-forgot.tpl.html";

        private Templates() {
        }

    }

    public static abstract class ServerSentEvent {
        public static final String RECENT_NOTIFICATIONS = "RECENT_NOTIFICATIONS";
        public static final String JOURNEY_CREATED = "JOURNEY_CREATED";

        private ServerSentEvent() {
        }
    }

}
