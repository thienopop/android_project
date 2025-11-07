package com.example.login.api;

import com.example.login.model.Tutor;
import com.example.login.model.SessionInfo;
import com.example.login.model.CourseInfo;
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
    @POST("/auth/register")
    Call<RegisterLoginResponse> registerUser(@Body User user);

    @POST("/auth/login")
    Call<RegisterLoginResponse> login(@Body User user);

    // ✅ API lấy danh sách session theo gia sư và ngày
    @GET("/sessions/by-tutor/date/{sessionDate}")
    Call<List<SessionInfo>> getSessionsByTutor(@Path("sessionDate") String sessionDate);
    @GET("/tutors/me")
    Call<Tutor> getTutorLogin();

//    @GET("/api/courses/my_courses/{status}")
//    Call<List<CourseInfo>> getCourseByTutor(@Path("status") String status);
////    http://localhost:8080/api/courses/my_courses?status=STUDENT_REGISTER
//

    @GET("/api/courses/my_courses/{status}")
    Call<List<CourseInfo>> getCourseByTutor(@Path("status") String status);

}
