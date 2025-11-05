package com.example.login;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.login.api.ApiService;
import com.example.login.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.login.model.SessionInfo;
import com.example.login.adapter.FagmentsSessionTodayAdapter;

import java.util.List;

public class TutorDashboarActivity extends AppCompatActivity {

    ListView listViewSessions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sessions_today);

        listViewSessions = findViewById(R.id.listViewSessions);

        // Gọi API
        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);

        String date = "2025-11-02";
        Call<List<SessionInfo>> call = apiService.getSessionsByTutor(date);

        call.enqueue(new Callback<List<SessionInfo>>() {
            @Override
            public void onResponse(Call<List<SessionInfo>> call, Response<List<SessionInfo>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<SessionInfo> sessionList = response.body();
                    FagmentsSessionTodayAdapter adapter = new FagmentsSessionTodayAdapter(TutorDashboarActivity.this, sessionList);
                    listViewSessions.setAdapter(adapter);

                    // Bắt sự kiện click item
                    listViewSessions.setOnItemClickListener((parent, view, position, id) -> {
                        SessionInfo ss = sessionList.get(position);
                        Toast.makeText(TutorDashboarActivity.this, "Bạn chọn session ID: " + ss.getId(), Toast.LENGTH_SHORT).show();
                    });

                } else {
                    Toast.makeText(TutorDashboarActivity.this, "⚠️ API trả về rỗng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<SessionInfo>> call, Throwable t) {
                Toast.makeText(TutorDashboarActivity.this, "❌ Lỗi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
