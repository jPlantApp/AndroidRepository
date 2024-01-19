package com.example.jplantapp.Api;

import com.example.jplantapp.Response.FlowerResponse;
import com.example.jplantapp.Response.UserReponse;
import com.example.jplantapp.Requests.PhotoClassificationRequest;
import com.example.jplantapp.Requests.UserLoginRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiEndpoint {

    @POST("api/get_photo_classification")
    Call<FlowerResponse> getPhotoClassification(@Body PhotoClassificationRequest request);
    @POST("api/login_user")
    Call<UserReponse> loginUser(@Body UserLoginRequest request);
    @GET("api/get_flower_list")
    Call<List<FlowerResponse>> getFlowerList(
            @Query("userToken") String tokenId,
            @Query("userEmail") String secondParameter
    );
}
