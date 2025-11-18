package com.example.login.fragments;

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

import com.example.login.R;
import com.example.login.api.ApiService;
import com.example.login.api.RetrofitClient;
import com.example.login.model.Tutor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment_st extends Fragment {

    private TextView tvTutorName;
    private TextView tvSdt;
    private TextView tvDiaChi;
    private TextView tvNgaySinh;
    private TextView tvBio;
    private TextView tvKinhNghiem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ View
        tvTutorName = view.findViewById(R.id.tvTutorName);
        tvSdt = view.findViewById(R.id.tvSdt);
        tvDiaChi = view.findViewById(R.id.tvDiaChi);
        tvNgaySinh = view.findViewById(R.id.tvNgaySinh);
        tvBio = view.findViewById(R.id.tvBio);
        tvKinhNghiem = view.findViewById(R.id.tvKinhNghiem);

        // Gọi API
        loadProfileData();
    }

    private void loadProfileData() {
        ApiService apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);
        Call<Tutor> call = apiService.getTutorLogin();

        call.enqueue(new Callback<Tutor>() {
            @Override
            public void onResponse(Call<Tutor> call, Response<Tutor> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Tutor tutor = response.body();
                    updateUI(tutor);
                } else {
                    Log.e("API_ERROR", "Response error: " + response.message());
                    Toast.makeText(getContext(), "⚠️ Không thể tải thông tin. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Tutor> call, Throwable t) {

                Log.e("API_FAILURE", "Error: " + t.getMessage());
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Cập nhật thông tin lên UI
    private void updateUI(Tutor tutor) {
        tvTutorName.setText(safeText(tutor.getFullName()));
        tvSdt.setText(safeText(tutor.getPhone()));
        tvDiaChi.setText(safeText(tutor.getAddress()));

        if (tutor.getDateOfBirth() != null) {
            String formattedDob = tutor.getDateOfBirth().toString();
            tvNgaySinh.setText(formattedDob);
        } else {
            tvNgaySinh.setText("Chưa cập nhật");
        }

        tvBio.setText(safeText(tutor.getBio()));
        tvKinhNghiem.setText(
                tutor.getExperienceYears() != null ? tutor.getExperienceYears() + " năm" : "Chưa cập nhật"
        );
    }

    private String safeText(String input) {
        return (input != null && !input.isEmpty()) ? input : "Chưa cập nhật";
    }

//
//    private void  loadProfileData() {
//        ApiService apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);
//
//        Call<Tutor> call = apiService.getTutorLogin();
//
//        call.enqueue(new Callback<Tutor>() {
//            @Override
//            public void onResponse(Call<Tutor> call, Response<Tutor> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    Tutor tutor = response.body();
//                    Toast.makeText(getContext(), "✅ Load OK: ", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getContext(), "⚠️ API trả về rỗng hoặc lỗi " + response.code(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Tutor> call, Throwable t) {
//                Toast.makeText(getContext(), "❌ Lỗi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                tvTutorName.setText(t.getMessage());
//            }
//        });
//    }


}
