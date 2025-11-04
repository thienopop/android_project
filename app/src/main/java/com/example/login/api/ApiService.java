package com.example.login.api;

import com.example.login.model.Session;
import com.example.login.model.SessionInfo;
import com.example.login.model.User;
import com.example.login.model.RegisterLoginResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public interface ApiService {
    @POST("/user/register")
    Call<RegisterLoginResponse> registerUser(@Body User user);

    @POST("/user/login")
    Call<RegisterLoginResponse> login(@Body User user);

    // ✅ API lấy danh sách session theo gia sư và ngày
    @GET("api/sessions/by-tutor/date/{sessionDate}")
    Call<List<SessionInfo>> getSessionsByTutor(@Path("sessionDate") String sessionDate);
}
