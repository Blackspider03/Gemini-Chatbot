package com.example.ruchi;

public interface ResponseCallback {
    void onResponse(String response);
    void onError(Throwable throwable);
}
