package com.example.login.fragments;

import android.app.Dialog;
import android.os.Bundle;
import com.example.login.api.PrefsHelper;
import com.example.login.LoginActivity;
import android.content.Intent;
import android.os.Bundle;
import com.example.login.api.PrefsHelper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.login.adapter.FagmentsSessionTodayAdapter;
import com.example.login.api.ApiService;
import com.example.login.api.RetrofitClient;
import com.example.login.fragments.HomeFragment;
import com.example.login.fragments.ChatboxFragment;
import com.example.login.fragments.ChatsFragment;
import com.example.login.fragments.ProfileFragment;
import com.example.login.fragments.CreateCourseFragment;

import com.example.login.model.ChatWithUserDetail;
import com.example.login.model.CourseInfo;
import com.example.login.TutorDashboardActivity;
import com.example.login.model.Notification;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.login.R;
import com.example.login.adapter.FagmentsSessionTodayAdapter;
import com.example.login.api.ApiService;
import com.example.login.api.RetrofitClient;
import com.example.login.model.SessionInfo;
import com.example.login.model.Tutor;

import java.time.format.DateTimeFormatter;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private TextView tvTutorName;
    private TextView tvSdt;
    private TextView tvDiaChi;
    private TextView tvNgaySinh;
    private TextView tvBio;
    private TextView tvKinhNghiem;
    private ImageView imMotification;
    private TextView tvLogOut;

//
//    android:id="@+id/btnlogOut"
//            // Lưu token vào SharedPreferences để dùng sau
//            PrefsHelper.saveToken(LoginActivity.this, token);
//            PrefsHelper.saveCurrentUserId(LoginActivity.this, currentUserId);
//    private TutorDashboardActivity

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
        imMotification= view.findViewById(R.id.imMotification);
        tvLogOut =view.findViewById(R.id.tvLogOut);


        // Gọi API
        loadProfileData();
        TutorDashboardActivity tutorDashboard =new TutorDashboardActivity();
        imMotification.setOnClickListener(v -> {
            FragmentManager fm = requireActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.main_container, new NotificationFragment());
            ft.addToBackStack(null);
            ft.commit();
        });

        tvLogOut.setOnClickListener(v -> {
            ConfirmLogOut();
//            FragmentManager fm = requireActivity().getSupportFragmentManager();
//            FragmentTransaction ft = fm.beginTransaction();
//            ft.replace(R.id.main_container, new NotificationFragment());
//            ft.addToBackStack(null);
//            ft.commit();
        });







//
//        imMotification.setOnClickListener(v -> {
//            ((TutorDashboardActivity) requireActivity())
//                    .loadFragment(new NotificationFragment());
//        });




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
                    Toast.makeText(getContext(), "Không thể tải thông tin. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
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
//                    Toast.makeText(getContext(), "Load OK: ", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getContext(), "API trả về rỗng hoặc lỗi " + response.code(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Tutor> call, Throwable t) {
//                Toast.makeText(getContext(), "Lỗi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                tvTutorName.setText(t.getMessage());
//            }
//        });`
//    }
private void loadChildFragment(Fragment fragment) {
    FragmentManager fm = getChildFragmentManager();
    FragmentTransaction ft = fm.beginTransaction();

    // .replace() sẽ tự động gỡ fragment cũ ra và thêm fragment mới vào
    ft.replace(R.id.main_container, fragment);

    // (Tùy chọn) Thêm vào back stack của trình quản lý con
    // ft.addToBackStack(null);

    ft.commit();
}



    private void ConfirmLogOut() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.confirm_logout_card);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(true);

        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnConfirm = dialog.findViewById(R.id.btnConfirm);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnConfirm.setOnClickListener(v -> {
            dialog.dismiss();   // Đóng dialog trước
            LogOut();           // Gọi hàm logout
        });
        dialog.show();
    }

    private void LogOut() {

        // Xoá token & userId
        PrefsHelper.clearCurrentUserId(requireContext());
        PrefsHelper.clearToken(requireContext());

        // Chuyển về màn Login
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        requireActivity().finish();
    }

}
