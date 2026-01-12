package com.example.login.api;

import com.example.login.model.Chat;
import com.example.login.model.ChatWithUserDetail;
import com.example.login.model.CourseWithTutorDetail;
import com.example.login.model.Feedback;
import com.example.login.model.Message;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import com.example.login.model. Verified;
import com.example.login.model.Tutor;
import com.example.login.model.Student;
import com.example.login.model.Course;
import com.example.login.model.SessionInfo;
import com.example.login.model.CourseInfo;
import com.example.login.model.DetailCourse;
import com.example.login.model.UploadFileResponse;
import com.example.login.model.Notification;
import com.example.login.model.SessionStatusCount;
import com.example.login.model.User;
import com.example.login.model.RegisterLoginResponse;
import com.example.login.model.UserWithId;

import java.util.List;
import java.util.Map;

import com.example.login.model.AddSession;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {
    // chỉnh song
    @POST("auth/register")
    Call<RegisterLoginResponse> registerUser(@Body User user);
    ///student/register
    /// giữa nguyên
    @POST("auth/login")
    Call<RegisterLoginResponse> login(@Body User user);

    @GET("courses/count_courses_by_status/{status}")
    Call<Integer> countCourseByStaus(@Path("status") String status);

    @POST("sessions/create")
    Call<Void> addSession(@Body AddSession addSession);
    @GET("auth/me")
    Call<UserWithId> getCurrentUser();
    @GET("notifications/by-user")
    Call<List<Notification>> getNotificationByUser();
    @GET("notifications/byId/{Id}")
    Call<Notification> getNotificationById(@Body int Id);

//    @POST("notifications/update/isread/{id}")
//    Call<Void> updateIsReadNotificationById(@Path("id") int Id);

    @PUT("notifications/update/isread/{id}")
    Call<Void> updateIsReadNotificationById(@Path("id") int id);
    @PUT("notifications/create")
    Call<Void> createNotification(@Body Notification noti);

//    ping("/create")

//    @PutMapping("/updateCourseStatus/{id}")

    @PUT("courses/updateCourseStatus/{courseId}")
    Call<Void> updateCourseStatus(@Path("courseId") int id,@Body String status);


//
//    @PUT("notifications/update/isread/{id}")
//    Call<Void> updateIsReadNotificationById(@Path("id") int id);


    @GET("sessions/courseId")
    Call<Integer> findCourseIdBySessionId(@Body int sessionId);

    // API lấy danh sách session theo gia sư và ngày

    @GET("sessions/by-tutor/date/{sessionDate}")
    Call<List<SessionInfo>> getSessionsByTutor(@Path("sessionDate") String sessionDate);

    //đã sửa/by-student/date/{sessionDate}")
    @GET("sessions/by-student/date/{sessionDate}")
    Call<List<SessionInfo>> getSessionsByStudent(@Path("sessionDate") String sessionDate);


//    @GetMapping("/by-course-id/{courseId}")
    @GET("sessions/by-course-id/{courseId}")
    Call<List<SessionInfo>> getSessionsByCourseId(@Path("courseId") int id);

//    http://localhost:8080/api/sessions/by-course-id/1

    @GET("tutors/me")
    Call<Tutor> getTutorLogin();

    @GET("tutors/count_new_tutor")
    Call<Integer> countNewTutor();
    @GET("students/count_new_student")
    Call<Integer> countNewStudent();

    @GET("tutors/count_verified_tutor")
    Call<List<Verified>> countVerified();



    @GET("students/me")
    Call<Student> getStudentLogin(
    );
    @GET("courses/userIdTutorByCourse/{courseId}")
    Call<Integer> getUserIdTutorByCourse(@Path("courseId") int courseId);

//    @GET("courses/my_courses/{status}")
//    Call<List<CourseInfo>> getCourseByTutor(@Path("status") String status);
    ////    http://localhost:8080/api/courses/my_courses?status=STUDENT_REGISTER
//


//    ")
    @GET("sessions/count_session_all_status")
    Call<List<SessionStatusCount>> countSessionAllStatus();


    @GET("courses/my_courses/{status}")
    Call<List<CourseInfo>> getCourseByTutor(@Path("status") String status);

    @GET("courses/by_all_admin/{status}")
    Call<List<CourseInfo>> getCourseByAdmin(@Path("status") String status);


    //lấy thông tin. khoá hco theo id của tutor
    @GET("courses/by_tutor/{id}")
    Call<DetailCourse> getDetailCourseByTutor(@Path("id") int id);
//    getDetailCourseByTutor(id);
//getDetailCourseByTutor(id);
    @GET("courses/by_student/{id}")
    Call<DetailCourse> getDetailCourseByStudent(@Path("id") int id);
//    @GetMapping("/by_student/{id}")
    @GET("courses/my_courses_student/{status}")
    Call<List<CourseInfo>> getCourseByStudent(@Path("status") String status);

    @GET("chats/my_chats")
    Call<List<ChatWithUserDetail>> getMyChats();


    @GET("chats/chat_id")
    Call<ChatWithUserDetail> getMyChatsId(int user_id);

    @GET("chats/{chatId}/messages")
    Call<List<Message>> getChatMessages(@Path("chatId") int chatId);

    @Multipart
    @POST("files/upload")
    Call<UploadFileResponse> uploadFile(@Part MultipartBody.Part file);
    @POST("courses/create")
    Call<Integer> AddCourse(@Body Course course);

    @POST("courses/update")
    Call<CourseInfo> UpdateCourse(@Body Course course);

    @GET("files/download/{filename}")
    Call<ResponseBody> downloadFile(@Path("filename") String filename);

    @GET("tutors/verified")
    Call<List<Tutor>> getVerifiedTutors();

    @GET("courses/available_courses")
    Call<List<CourseWithTutorDetail>> getAvailableCoursesForStudent();

    @PUT("courses/register_course_by_student/{courseId}")
    Call<Void> registerCourse(@Path("courseId") int courseId);

    @GET("tutors/{tutorId}")
    Call<Tutor> getTutorById(@Path("tutorId") int tutorId);

    @GET("courses/by_tutor_id/{tutorId}")
    Call<List<Course>> getCoursesByTutorId(@Path("tutorId") int tutorId);

    @GET("chats/between/{user1Id}/{user2Id}")
    Call<Chat> getChatBetweenUsers(@Path("user1Id") int user1Id, @Path("user2Id") int user2Id);

    @POST("chats/create_with_first_message")
    Call<Chat> createChatWithFirstMessage(@Body Map<String, Object> payload); // Payload: { senderId: 9, receiverId: 10, messageContent: "Hello" }

    @POST("feedbacks/create")
    Call<Void> createFeedback(@Body Feedback feedback);

    @GET("feedbacks/of_course/{courseId}")
    Call<Feedback> getFeedbackByCourseId(@Path("courseId") int courseId);

    @GET("courses/{courseId}")
    Call<Course> getCourseById(@Path("courseId") int courseId);
}