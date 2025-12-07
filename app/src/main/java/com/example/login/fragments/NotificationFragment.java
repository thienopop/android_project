package com.example.login.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.login.DetailCourseActivity;
import com.example.login.R;
import com.example.login.api.ApiService;
import com.example.login.api.RetrofitClient;
import com.example.login.model.CourseIdCallback;
import com.example.login.model.Notification;
import com.example.login.model.Tutor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;




import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.login.R;
import com.example.login.adapter. ListNotificationAdapter;
import com.example.login.api.ApiService;
import com.example.login.api.RetrofitClient;
import com.example.login.model.SessionInfo;
import com.example.login.model.Tutor;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment   extends Fragment {

    private ListView listViewNotification;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.notification_fragment, container, false);
        listViewNotification = view.findViewById(R.id.listViewNotification);

        loadNotification();

        return view; // 👈 BẮT BUỘC PHẢI CÓ
    }


    private void loadNotification() {
        // Kiểm tra context trước khi gọi API
        if (getContext() == null) {
            return;
        }

        ApiService apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);
        Call<List<Notification>> call = apiService.getNotificationByUser();

        call.enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(@NonNull Call<List<Notification>> call, @NonNull Response<List<Notification>> response) {
                if (!isAdded()) return; // Đảm bảo fragment vẫn còn attached

                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    List<Notification> notification = response.body();
//                    txtMessage.setText("");
                    ListNotificationAdapter adapter = new ListNotificationAdapter(getContext(), notification);
                    listViewNotification.setAdapter(adapter);

                    listViewNotification.setOnItemClickListener((parent, view, position, id) -> {
                        Notification ss = notification.get(position);
//                        int sessionId= ss.getId();
//                        loadCourseId(sessionId, courseId -> {
//
//                            Intent intent = new Intent(requireContext(), DetailCourseActivity.class);
//
//                            intent.putExtra("COURSE_ID_KEY", courseId);
//                            startActivity(intent);
//
//                        });
                    });
                } else {
                    // Xóa list cũ nếu không có dữ liệu mới
                    listViewNotification.setAdapter(null);
//                    txtMessage.setText("Không có lịch học cho ngày :" + date);
//                    Toast.makeText(getContext(), "Không có lịch học cho ngày " + date, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Notification>> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(getContext(), "❌ Lỗi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}