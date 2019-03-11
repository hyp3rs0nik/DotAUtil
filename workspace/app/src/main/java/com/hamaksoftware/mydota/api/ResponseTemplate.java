package com.hamaksoftware.mydota.api;

import com.google.gson.annotations.Expose;

public class ResponseTemplate<T>{
    @Expose
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
