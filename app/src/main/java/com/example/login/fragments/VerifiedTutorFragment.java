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
import com.example.login.model.SessionStatusCount;
import com.example.login.model.Tutor;
import com.example.login.model.Verified;
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
import java.util.List;

// Retrofit
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifiedTutorFragment extends Fragment {
    private TextView  tx_verified,tx_unverified;
    private int numberOfUnverified=23;
    private int numberOfVerified=100;
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

        pieChart = view.findViewById(R.id.pieChart);

//        show(2,8);
        loadVerified();


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






    private void loadVerified() {

        ApiService apiService =
                RetrofitClient.getClient(requireContext()).create(ApiService.class);

        Call<List<Verified>> call = apiService.countVerified();

        call.enqueue(new Callback<List<Verified>>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<Verified>> call,
                    @NonNull Response<List<Verified>> response) {

                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null) {

                    List<Verified> list = response.body();

                    int verified = 0;
                    int unVerified=0;

                    for (Verified item : list) {
                        if (item.getVerified()==1) {
                            verified = item.getTotal();
                        } else {
                            unVerified = item.getTotal();
                        }
                    }
                    show( verified,unVerified);

                } else {
                    Toast.makeText(
                            requireContext(),
                            "❌ Lỗi API: " + response.code(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<List<Verified>> call,
                    @NonNull Throwable t) {

                if (!isAdded()) return;

                Toast.makeText(
                        requireContext(),
                        "❌ Lỗi mạng: " + t.getMessage(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }




    private void show(int verified,int unverified){
        tx_verified.setText(String.valueOf(verified));
        tx_unverified.setText(String.valueOf(unverified));

// 1️⃣ Bật chế độ phần trăm
        pieChart.setUsePercentValues(true);

// 2️⃣ Dữ liệu
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(verified, "Verified"));
        entries.add(new PieEntry(unverified, "Unverified"));

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

}

