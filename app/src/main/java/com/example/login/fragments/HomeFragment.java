package com.example.login.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button; // <-- Thêm import

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.login.R;

public class HomeFragment extends Fragment {
//
//    private TextView tvCourse;
//    private TextView tvSession;
    private Button btnShowFragmentA, btnShowFragmentB; // <-- Thêm nút

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sup_home, container, false);

//        // Ánh xạ các view
//        tvCourse = view.findViewById(R.id.tvCourse);
//        tvSession = view.findViewById(R.id.tvSession);
        btnShowFragmentA = view.findViewById(R.id.btnShowFragmentA);
        btnShowFragmentB = view.findViewById(R.id.btnShowFragmentB);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Gán sự kiện click
        btnShowFragmentA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo mới và tải Fragment A
                loadChildFragment(new ShowSessionsFragment());
            }
        });

        btnShowFragmentB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo mới và tải Fragment B
                loadChildFragment(new ShowCourseFragment());
            }
        });

        // Tải một fragment mặc định khi bắt đầu
        if (savedInstanceState == null) {
            loadChildFragment(new ShowSessionsFragment()); // Tải Fragment A làm mặc định
        }
    }

    /**
     * Hàm này giờ sẽ thay thế bất kỳ fragment nào được truyền vào
     * vào trong 'child_fragment_container'.
     */
    private void loadChildFragment(Fragment fragment) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        // .replace() sẽ tự động gỡ fragment cũ ra và thêm fragment mới vào
        ft.replace(R.id.child_fragment_container, fragment);

        // (Tùy chọn) Thêm vào back stack của trình quản lý con
        // ft.addToBackStack(null);

        ft.commit();
    }
}