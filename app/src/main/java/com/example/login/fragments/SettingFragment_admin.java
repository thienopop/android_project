package com.example.login.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.login.LoginActivity;
import com.example.login.R;
import com.example.login.TutorDashboardActivity;
import com.example.login.api.ApiService;
import com.example.login.api.PrefsHelper;
import com.example.login.api.RetrofitClient;
import com.example.login.model.Tutor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;





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

public class SettingFragment_admin extends Fragment {

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
//            // 🔒 Lưu token vào SharedPreferences để dùng sau
//            PrefsHelper.saveToken(LoginActivity.this, token);
//            PrefsHelper.saveCurrentUserId(LoginActivity.this, currentUserId);
//    private TutorDashboardActivity

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvLogOut =view.findViewById(R.id.tvLogOut);
        tvLogOut.setOnClickListener(v -> {
            ConfirmLogOut();
//            FragmentManager fm = requireActivity().getSupportFragmentManager();
//            FragmentTransaction ft = fm.beginTransaction();
//            ft.replace(R.id.main_container, new NotificationFragment());
//            ft.addToBackStack(null);
//            ft.commit();
        });






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
