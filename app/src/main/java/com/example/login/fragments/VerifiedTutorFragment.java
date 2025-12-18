package com.example.login.fragments;

import android.graphics.Color;
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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;





import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

// AndroidX
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

// App
import com.example.login.R;
import com.example.login.api.ApiService;
import com.example.login.api.RetrofitClient;
import com.example.login.model.Tutor;

// MPAndroidChart
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

// Java
import java.util.ArrayList;

// Retrofit
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifiedTutorFragment extends Fragment {
    private TextView  tx_verified,tx_unverified;
    private int numberOfUnverified=23;
    private int numberOfVerified=121;
    private PieChart pieChart ;
    private BarChart barChart;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_verification, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ View
        tx_verified = view.findViewById(R.id.tx_verified);
        tx_unverified= view.findViewById(R.id.tx_unverified);
//        loadNumberOfNewTutor();
//        loadNumberOfNewCourse();
        tx_verified.setText(String.valueOf(numberOfVerified));
        tx_unverified.setText(String.valueOf(numberOfUnverified));
        pieChart = view.findViewById(R.id.pieChart);
        show(numberOfVerified,numberOfUnverified);
//        barChart = view.findViewById(R.id.barChart);
//        showRateVerification(38,62);




//        imMotification.setOnClickListener(v -> {
//            FragmentManager fm = requireActivity().getSupportFragmentManager();
//            FragmentTransaction ft = fm.beginTransaction();
//            ft.replace(R.id.main_container, new NotificationFragment());
//            ft.addToBackStack(null);
//            ft.commit();
//        });
//
//        tvLogOut.setOnClickListener(v -> {
//            ConfirmLogOut();
////            FragmentManager fm = requireActivity().getSupportFragmentManager();
////            FragmentTransaction ft = fm.beginTransaction();
////            ft.replace(R.id.main_container, new NotificationFragment());
////            ft.addToBackStack(null);
////            ft.commit();
//        });
//


    }

    private void showRateVerification( int completed, int unCompleted, int cancel )
    {


// 1️⃣ Giá trị từng cột
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, completed)); // Lớp A
        entries.add(new BarEntry(1f, unCompleted)); // Lớp B

        BarDataSet dataSet = new BarDataSet(entries, "%");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.BLACK);

// 2️⃣ Gán dữ liệu cho chart
        BarData data = new BarData(dataSet);
        barChart.setData(data);

// 3️⃣ Nội dung chữ dưới từng cột (TRỤC X)
        ArrayList<String> labels = new ArrayList<>();
        labels.add("Verified");
        labels.add("Unverified");
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

// 4️⃣ Cấu hình thêm
        barChart.getDescription().setEnabled(false);
//        barChart.animateY(1000);
        barChart.invalidate();

    }




    private void show(int numberOfVerified, int numberOfUnverified){


// 1️⃣ Bật chế độ phần trăm
        pieChart.setUsePercentValues(true);

// 2️⃣ Dữ liệu
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(numberOfVerified, "Verified"));
        entries.add(new PieEntry(numberOfUnverified, "Unverified"));

// 3️⃣ Dataset
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.WHITE);

// 4️⃣ Data + Formatter %
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(pieChart));
        pieChart.setData(data);

// 5️⃣ Cấu hình giao diện
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(40f);
        pieChart.setTransparentCircleRadius(45f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(12f);

// 6️⃣ Animation
        pieChart.animateY(1000);
        pieChart.invalidate();

    }






    private void loadNumberOfNewTutor() {
        ApiService apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);
        Call<Tutor> call = apiService.getTutorLogin();

        call.enqueue(new Callback<Tutor>() {
            @Override
            public void onResponse(Call<Tutor> call, Response<Tutor> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Tutor tutor = response.body();

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


    private void loadNumberOfNewCourse() {
        ApiService apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);
        Call<Tutor> call = apiService.getTutorLogin();

        call.enqueue(new Callback<Tutor>() {
            @Override
            public void onResponse(Call<Tutor> call, Response<Tutor> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Tutor tutor = response.body();

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
//    private void loadChildFragment(Fragment fragment) {
//        FragmentManager fm = getChildFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//
//        // .replace() sẽ tự động gỡ fragment cũ ra và thêm fragment mới vào
//        ft.replace(R.id.main_container, fragment);
//
//        // (Tùy chọn) Thêm vào back stack của trình quản lý con
//        // ft.addToBackStack(null);
//
//        ft.commit();
//    }
}
