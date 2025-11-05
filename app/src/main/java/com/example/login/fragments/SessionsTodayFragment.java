package com.example.login.fragments;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.login.api.ApiService;
import com.example.login.api.RetrofitClient;
import com.example.login.model.SessionInfo;
import com.example.login.adapter.FagmentsSessionTodayAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.login.R;

public class SessionsTodayFragment extends Fragment {

    private ListView listViewSessions;

    public SessionsTodayFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate layout cho Fragment
        View view = inflater.inflate(R.layout.fragment_sessions_today, container, false);
        listViewSessions = view.findViewById(R.id.listViewSessions);

        loadSessions();

        return view;
    }

    private void loadSessions() {
        ApiService apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);

        String date = "2025-11-02"; // lấy ngày hôm nay



        Call<List<SessionInfo>> call = apiService.getSessionsByTutor(date);

        call.enqueue(new Callback<List<SessionInfo>>() {
            @Override
            public void onResponse(Call<List<SessionInfo>> call, Response<List<SessionInfo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<SessionInfo> sessionList = response.body();
                    FagmentsSessionTodayAdapter adapter = new FagmentsSessionTodayAdapter(getContext(), sessionList);
                    listViewSessions.setAdapter(adapter);

                    listViewSessions.setOnItemClickListener((parent, view, position, id) -> {
                        SessionInfo ss = sessionList.get(position);
                        Toast.makeText(getContext(), "Bạn chọn session ID: " + ss.getId(), Toast.LENGTH_SHORT).show();
                    });
                } else {
                    Toast.makeText(getContext(), "⚠️ API trả về rỗng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<SessionInfo>> call, Throwable t) {
                Toast.makeText(getContext(), "❌ Lỗi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
