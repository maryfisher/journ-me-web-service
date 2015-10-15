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

    public JournMeExceptionDto(ExcpetionCode code) {
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

    public enum ExcpetionCode {
        CLIENT_SERVER_PROBLEM("110"),
        INTERNAL_SYSTEM_PROBLEM("120"),
        UPSTREAM_SYSTEM_PROBLEM("130"),
        AUTHENTICATION_FAILED("210"),
        AUTH_TOKEN_INVALID("220"),
        FILE_TYPE_CORRUPTED_INVALID("310"),
        FILE_TOO_BIG("311"),
        FILE_EMPTY("312");

        private final String code;

        ExcpetionCode(String code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return code;
        }
    }
}
