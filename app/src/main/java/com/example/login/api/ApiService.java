package com.example.login.api;


import com.example.login.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("users")
    Call<User> registerUser(@Body User user);
}
