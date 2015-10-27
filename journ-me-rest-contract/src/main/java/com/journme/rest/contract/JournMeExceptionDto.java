package com.journme.rest.contract;

import java.util.Map;

/**
 * <h1>POJO for REST serialisation</h1>
 * This class represents the data properties for error/warn/info codes to be exchanged between server and client
 *
 * @author PHT
 * @version 1.0
 * @since 15.10.2015
 */
public class JournMeExceptionDto {

    private final String code;
    private Map<String, Object> interpolationMap;

    public JournMeExceptionDto(ExceptionCode code) {
        this.code = code.toString();
    }

    public String getCode() {
        return code;
    }

    public Map<String, Object> getInterpolationMap() {
        return interpolationMap;
    }

    public void setInterpolationMap(Map<String, Object> interpolationMap) {
        this.interpolationMap = interpolationMap;
    }

    public enum ExceptionCode {
        //Technical issues 1xx
        CLIENT_SERVER_PROBLEM("110"),
        INTERNAL_SYSTEM_PROBLEM("120"),
        UPSTREAM_SYSTEM_PROBLEM("130"),

        //Security issues 2xx
        AUTHENTICATION_FAILED("210"),
        AUTH_TOKEN_INVALID("220"),

        //Incoming value issues 3xx
        ALIAS_NONEXISTENT("310"),
        JOURNEY_NONEXISTENT("311"),
        EMAIL_TAKEN("320"),
        FILE_TYPE_CORRUPTED_INVALID("330"),
        FILE_TOO_BIG("331"),
        FILE_EMPTY("332");

        private final String code;

        ExceptionCode(String code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return code;
        }
    }
}
