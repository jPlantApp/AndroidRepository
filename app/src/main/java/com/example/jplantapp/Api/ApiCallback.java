package com.example.jplantapp.Api;

public interface ApiCallback<T> {
    void onSuccess(T result);
    void onError(String errorMessage);
}
