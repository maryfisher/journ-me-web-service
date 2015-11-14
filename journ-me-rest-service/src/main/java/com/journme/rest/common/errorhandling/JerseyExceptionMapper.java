package com.journme.rest.common.errorhandling;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.jaxrs.base.JsonMappingExceptionMapper;
import com.fasterxml.jackson.jaxrs.base.JsonParseExceptionMapper;
import com.journme.rest.contract.JournMeExceptionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * <h1>Jersey global exception mapper</h1>
 *
 * @author PHT
 * @version 1.0
 * @since 26.10.2015
 */
@Provider
public class JerseyExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JerseyExceptionMapper.class);

    @Override
    public Response toResponse(Throwable ex) {
        Response.ResponseBuilder rb;

        if (ex instanceof JournMeException) {
            JournMeException jmEx = (JournMeException) ex;
            if (Response.Status.Family.SERVER_ERROR == jmEx.getHttpStatus().getFamily()) {
                LOGGER.warn("Unhandled JM exception: ", jmEx);
            } else {
                LOGGER.info("Handled JM exception: ", jmEx);
            }

            rb = Response.
                    status(jmEx.getHttpStatus()).
                    entity(new JournMeExceptionDto(jmEx.getCode()));
        } else if (ex instanceof ClientErrorException) {
            LOGGER.info("Handled client call issue: ", ex);
            rb = Response.
                    status(((ClientErrorException) ex).getResponse().getStatus()).
                    entity(new JournMeExceptionDto(JournMeExceptionDto.ExceptionCode.CLIENT_SERVER_PROBLEM));
        } else {
            LOGGER.warn("Unhandled application exception: ", ex);
            rb = Response.
                    status(Response.Status.INTERNAL_SERVER_ERROR).
                    entity(new JournMeExceptionDto(JournMeExceptionDto.ExceptionCode.INTERNAL_SYSTEM_PROBLEM));
        }

        return rb.type(MediaType.APPLICATION_JSON).build();
    }

    // Overwrite the default JsonParseExceptionMapper that is registered via JacksonFeature
    @Provider
    public static class JacksonParseExceptionMapper extends JsonParseExceptionMapper {

        private static final Logger LOGGER = LoggerFactory.getLogger(JacksonParseExceptionMapper.class);

        @Override
        public Response toResponse(JsonParseException ex) {
            LOGGER.warn("Json parsing exception: ", ex);
            return Response.
                    status(Response.Status.INTERNAL_SERVER_ERROR).
                    entity(new JournMeExceptionDto(JournMeExceptionDto.ExceptionCode.INTERNAL_SYSTEM_PROBLEM)).
                    type(MediaType.APPLICATION_JSON).
                    build();
        }
    }

    // Overwrite the default JsonMappingExceptionMapper that is registered via JacksonFeature
    @Provider
    public static class JacksonMappingExceptionMapper extends JsonMappingExceptionMapper {

        private static final Logger LOGGER = LoggerFactory.getLogger(JacksonParseExceptionMapper.class);

        @Override
        public Response toResponse(JsonMappingException ex) {
            LOGGER.warn("Json mapping exception: ", ex);
            return Response.
                    status(Response.Status.INTERNAL_SERVER_ERROR).
                    entity(new JournMeExceptionDto(JournMeExceptionDto.ExceptionCode.INTERNAL_SYSTEM_PROBLEM)).
                    type(MediaType.APPLICATION_JSON).
                    build();
        }
    }
}
