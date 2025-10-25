package com.example.login.api;


import com.example.login.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("user")
    Call<User> registerUser(@Body User user);
    @POST("login")
    Call<User> login(@Body User user);

}
