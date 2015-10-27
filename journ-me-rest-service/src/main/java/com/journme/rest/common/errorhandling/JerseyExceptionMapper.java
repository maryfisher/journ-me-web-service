package com.journme.rest.common.errorhandling;

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
            LOGGER.info("Handled JM exception: ", ex);
            JournMeException jmEx = (JournMeException) ex;
            rb = Response.
                    status(jmEx.getHttpStatus()).
                    entity(new JournMeExceptionDto(jmEx.getCode()));
        } else if (ex instanceof ClientErrorException) {
            LOGGER.info("Handled client call issue: ", ex);
            rb = Response.
                    status(((ClientErrorException) ex).getResponse().getStatus()).
                    entity(new JournMeExceptionDto(JournMeExceptionDto.ExceptionCode.UPSTREAM_SYSTEM_PROBLEM));
        } else {
            LOGGER.warn("Unhandled application exception: ", ex);
            rb = Response.
                    status(Response.Status.INTERNAL_SERVER_ERROR).
                    entity(new JournMeExceptionDto(JournMeExceptionDto.ExceptionCode.INTERNAL_SYSTEM_PROBLEM));
        }

        return rb.type(MediaType.APPLICATION_JSON).build();
    }
}
