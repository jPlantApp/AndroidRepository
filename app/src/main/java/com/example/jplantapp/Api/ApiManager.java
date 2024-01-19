package com.example.jplantapp.Api;

import com.example.jplantapp.Response.FlowerResponse;
import com.example.jplantapp.Response.UserReponse;
import com.example.jplantapp.Requests.PhotoClassificationRequest;
import com.example.jplantapp.Requests.UserLoginRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager<T> {
    private static final String BASE_URL = "http://20.16.13.227:5000/";
    private final Retrofit retrofit;

    public ApiManager() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public <T> void sendRequest(Call<T> call, final ApiCallback<T> callback) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful()) {
                    T result = response.body();
                    if (result != null) {
                        callback.onSuccess(result);
                    }
                } else {
                    callback.onError("Request failed");
                }
            }
            @Override
            public void onFailure(Call<T> call, Throwable t) {
                callback.onError("Request failed: " + t.getMessage());
            }
        });
    }

    public void sendPhoto(String base64Image, String lastImagePath, String userToken, String userEmail, final ApiCallback<FlowerResponse> callback) {
        ApiEndpoint apiEndpoint = retrofit.create(ApiEndpoint.class);
        PhotoClassificationRequest request = new PhotoClassificationRequest(base64Image, lastImagePath, userToken, userEmail);
        Call<FlowerResponse> call = apiEndpoint.getPhotoClassification(request);
        sendRequest(call, callback);
    }

    public void loginUser(String userEmail, String userToken, String userName, final ApiCallback<UserReponse> callback) {
        ApiEndpoint apiEndpoint = retrofit.create(ApiEndpoint.class);
        UserLoginRequest request = new UserLoginRequest(userEmail, userName, userToken);
        Call<UserReponse> call = apiEndpoint.loginUser(request);
        sendRequest(call, callback);
    }

    public void getFlowerList(String userToken, String userEmail, final ApiCallback<List<FlowerResponse>> callback) {
        ApiEndpoint apiEndpoint = retrofit.create(ApiEndpoint.class);
        Call<List<FlowerResponse>> call = apiEndpoint.getFlowerList(userToken, userEmail);
        sendRequest(call, callback);
    }
}