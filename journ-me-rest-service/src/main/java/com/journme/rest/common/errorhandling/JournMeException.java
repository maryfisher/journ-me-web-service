package com.journme.rest.common.errorhandling;

import com.journme.rest.contract.JournMeExceptionDto.ExceptionCode;

import javax.ws.rs.core.Response;

public class JournMeException extends RuntimeException {

    private static final long serialVersionUID = 7039984300770628781L;

    private final ExceptionCode code;

    private final Response.Status httpStatus;

    public JournMeException(String message, Response.Status httpStatus, ExceptionCode code) {
        super(message);
        this.httpStatus = httpStatus;
        this.code = code;
    }

    public JournMeException(String message, Response.Status httpStatus, ExceptionCode code, Throwable throwable) {
        super(message, throwable);
        this.httpStatus = httpStatus;
        this.code = code;
    }

    public Response.Status getHttpStatus() {
        return httpStatus;
    }

    public ExceptionCode getCode() {
        return code;
    }
}
