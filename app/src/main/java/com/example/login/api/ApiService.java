package com.example.login.api;


import com.example.login.model.User;
import com.example.login.model.RegisterLoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/user/register")
    Call<RegisterLoginResponse> registerUser(@Body User user);
    @POST("/user/login")
    Call<RegisterLoginResponse> login(@Body User user);

}
