package com.hamaksoftware.mydota.api;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class APIRequestException extends Exception{
    private final RetrofitError error;
    public APIRequestException(RetrofitError cause){
        super(cause);
        this.error = cause;
    }

    public APIErrorCode getStatus(){
        if((this.error.getCause() instanceof SocketTimeoutException) || (this.error.getCause() instanceof ConnectException)){
            return APIErrorCode.SOCKET_TIMEOUT;
        }

        if(error.isNetworkError()){
            return APIErrorCode.NO_CONNECTION;
        }

        Response response = error.getResponse();
        if(response != null){
            return APIErrorCode.fromCode(response.getStatus());
        }

        return APIErrorCode.UNKNOWN;
    }

    public Response getRawResponse(){
        return this.error.getResponse();
    }


}
