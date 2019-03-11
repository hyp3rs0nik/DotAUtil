package com.hamaksoftware.mydota.api;

        import retrofit.ErrorHandler;
        import retrofit.RetrofitError;
        import retrofit.client.Response;

public class APIErrorHandler implements ErrorHandler {

    @Override
    public Throwable handleError(RetrofitError cause) {
        if (cause.isNetworkError()) {
            return new APIRequestException(cause);
        } else {
            Response r = cause.getResponse();
            if (r != null) {
                if (r.getStatus() == APIErrorCode.FORBIDDEN.getCode()
                        || r.getStatus() == APIErrorCode.NOT_FOUND.getCode()
                        || r.getStatus() == APIErrorCode.METHOD_NOT_ALLOWED.getCode()
                        || r.getStatus() == APIErrorCode.SERVER_ERROR.getCode()
                        || r.getStatus() == APIErrorCode.UNAUTHORIZED.getCode()
                        || r.getStatus() == APIErrorCode.SERVICE_NOT_AVAILABLE.getCode()
                        || r.getStatus() == APIErrorCode.BAD_REQUEST.getCode()) {

                    return new APIRequestException(cause);

                }
            }
        }

        return cause;

    }

}

