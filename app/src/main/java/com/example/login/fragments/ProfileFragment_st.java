package com.example.login.fragments;
import android.app.Dialog;
import android.os.Bundle;
import com.example.login.api.PrefsHelper;
import com.example.login.LoginActivity;
import android.content.Intent;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.login.api.ApiService;
import com.example.login.api.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.login.R;
import com.example.login.model.Student;
public class ProfileFragment_st extends Fragment {
    private TextView tvStudentName;
    private TextView tvSdt;
    private TextView tvDiaChi;
    private TextView tvNgaySinh;
    private TextView tvBio;
    private TextView tvKinhNghiem;
    private ImageView imMotification;
    private TextView tvLogOut;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_st, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Ánh xạ View
        tvStudentName = view.findViewById(R.id.tvStudentName);
        tvSdt = view.findViewById(R.id.tvSdt);
        tvDiaChi = view.findViewById(R.id.tvDiaChi);
        tvNgaySinh = view.findViewById(R.id.tvNgaySinh);
        tvBio = view.findViewById(R.id.tvBio);
        tvKinhNghiem = view.findViewById(R.id.tvKinhNghiem);
        imMotification = view.findViewById(R.id.imMotification);
        tvLogOut = view.findViewById(R.id.tvLogOut);
        // Load dữ liệu
        loadProfileData();
        imMotification.setOnClickListener(v -> {
            FragmentManager fm = requireActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.main_container, new NotificationFragment());
            ft.addToBackStack(null);
            ft.commit();
        });
        tvLogOut.setOnClickListener(v -> ConfirmLogOut());
    }
    private void loadProfileData() {
        ApiService apiService = RetrofitClient.getClient(requireContext()).create(ApiService.class);
        Call<Student> call = apiService.getStudentLogin();

        call.enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Student student = response.body();
                    updateUI(student);
                } else {
                    Toast.makeText(requireContext(), "Không thể tải thông tin.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(Student student) {
        tvStudentName.setText(safeText(student.getFullName()));
        tvSdt.setText(safeText(student.getPhone()));
        tvDiaChi.setText(safeText(student.getAddress()));

        if (student.getDateOfBirth() != null) {
            tvNgaySinh.setText(student.getDateOfBirth());
        } else {
            tvNgaySinh.setText("Chưa cập nhật");
        }

        tvBio.setText(safeText(student.getDescription()));
        tvKinhNghiem.setText(safeText(student.getGrade()));
    }

    private String safeText(String input) {
        return (input != null && !input.isEmpty()) ? input : "Chưa cập nhật";
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
            dialog.dismiss();
            LogOut();
        });

        dialog.show();
    }

    private void LogOut() {
        PrefsHelper.clearCurrentUserId(requireContext());
        PrefsHelper.clearToken(requireContext());
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        requireActivity().finish();
    }
}
