package com.hamaksoftware.mydota.api;
public enum APIErrorCode {
    NO_CONNECTION(-2, "Unable to Connect to the Internet."),
    SOCKET_TIMEOUT(-1, "Connection to Server Timeout."),
    UNKNOWN(0, "Unknown"),
    OK(200, "OK"),
    CREATED(201,"Created"),
    NOT_MODIFIED(304, "Not Modified"),
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Resource Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    REQUEST_TIMEOUT(408, "Request Timeout"),
    SERVER_ERROR(500, "Internal Server Error"),
    SERVICE_NOT_AVAILABLE(501, "Service Not Available");

    private final int statusCode;
    private final String description;

    private APIErrorCode(int code, String description) {
        this.statusCode = code;
        this.description = description;
    }

    public int getCode(){
        return statusCode;
    }

    public String getDescription(){
        return description;
    }

    @Override
    public String toString(){
        return statusCode + " - " + description;
    }

    public static APIErrorCode fromCode(int code) {
        for(APIErrorCode type : APIErrorCode.values()) {
            if(type.getCode() == code) {
                return type;
            }
        }
        return APIErrorCode.UNKNOWN;
    }
}
