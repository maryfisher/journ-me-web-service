package com.journme.rest.config;

import com.journme.rest.RootResource;
import com.journme.rest.contract.JournMeExceptionDto;
import com.journme.rest.contract.JournMeExceptionDto.ExcpetionCode;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * <h1>Jersey configuration</h1>
 * This class contains all Jersey REST related configurations
 *
 * @author PHT
 * @version 1.0
 * @since 15.10.2015
 */
@Configuration
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        //Jersey REST endpoints
        register(RootResource.class);

        //Jersey filters


        //Jersey features
        register(JacksonFeature.class);
        register(MultiPartFeature.class);

        //Jersey exception mappings
        register(JerseyExceptionMapper.class);
    }

    @Provider
    private static class JerseyExceptionMapper implements ExceptionMapper<Throwable> {

        private static final Logger LOGGER = LoggerFactory.getLogger(JerseyExceptionMapper.class);

        @Override
        public Response toResponse(Throwable ex) {
            Response.ResponseBuilder rb;

            if (ex instanceof IllegalArgumentException) {
                LOGGER.info("Handled illegal argument: ", ex);
                rb = Response.
                        status(Response.Status.BAD_REQUEST).
                        entity(new JournMeExceptionDto(ExcpetionCode.FILE_TYPE_CORRUPTED_INVALID));
            } else if (ex instanceof ClientErrorException) {
                LOGGER.info("Handled client call issue: ", ex);
                rb = Response.
                        status(((ClientErrorException) ex).getResponse().getStatus()).
                        entity(new JournMeExceptionDto(ExcpetionCode.UPSTREAM_SYSTEM_PROBLEM));
            } else {
                LOGGER.warn("Unhandled application exception: ", ex);
                rb = Response.
                        status(Response.Status.INTERNAL_SERVER_ERROR).
                        entity(new JournMeExceptionDto(ExcpetionCode.INTERNAL_SYSTEM_PROBLEM));
            }

            return rb.type(MediaType.APPLICATION_JSON).build();
        }
    }

}
