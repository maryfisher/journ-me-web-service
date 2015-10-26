package com.journme.rest.common.errorhandling;

import com.journme.rest.contract.JournMeExceptionDto;

import javax.ws.rs.core.Response;

public class JournMeException extends IllegalArgumentException {

    private final JournMeExceptionDto.ExceptionCode code;

    private final Response.Status httpStatus;

    public JournMeException(String message, Response.Status httpStatus, JournMeExceptionDto.ExceptionCode code) {
        super(message);
        this.httpStatus = httpStatus;
        this.code = code;
    }

    public Response.Status getHttpStatus() {
        return httpStatus;
    }

    public JournMeExceptionDto.ExceptionCode getCode() {
        return code;
    }
}
