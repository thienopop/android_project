package com.example.login.fragments;


// Android
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import java.util.List;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.ForgotPasswordActivity;
import com.example.login.ManagermentConfirmCourse_admin;
import com.example.login.model.CourseIdCallback;
import com.example.login.model.DetailCourse;
import com.example.login.model.SessionStatusCount;
import com.example.login.model.SessionStatusCount;
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
import android.widget.LinearLayout;


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
import retrofit2.http.Path;

public class AdminDashboardFragment extends Fragment {
    private TextView numberOfNewCourse, numberOfNewTutor,numberOfNewStudent;

    LinearLayout ln_showNewTutor,ln_showNewCourse;
    private int numberNewCourse=10;
    private int numberNewTutor=10;
    private int numberNewStudent=10;

    private  PieChart pieChart ;
    private  BarChart barChart;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_dashboard, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ View
  numberOfNewTutor = view.findViewById(R.id.tx_new_tutor);
        numberOfNewCourse  = view.findViewById(R.id.tx_new_course);
        numberOfNewStudent  = view.findViewById(R.id.tx_new_student);

        ln_showNewTutor  = view.findViewById(R.id. ln_showNewTutor);
        ln_showNewCourse  = view.findViewById(R.id. ln_showNewCourse);

//        loadNumberOfNewTutor();
//        loadNumberOfNewCourse();
        loadCountCourseByStatus("UNCONFIRM");
        loadCountNewTutor();
        loadCountNewStudent();
        loadCountSession();


    pieChart = view.findViewById(R.id.pieChart);





//        ln_showNewTutor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
        ln_showNewCourse.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ManagermentConfirmCourse_admin.class);
            startActivity(intent);
//            requireActivity().finish();
        });


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




    private void show(int completed, int unCompleted, int canceled){


// 1️⃣ Bật chế độ phần trăm
        pieChart.setUsePercentValues(true);

// 2️⃣ Dữ liệu
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(completed, "Completed"));
        entries.add(new PieEntry(unCompleted, "Uncompleted"));
        entries.add(new PieEntry(canceled, "Canceled"));

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

    private void loadCountCourseByStatus(String status) {

        ApiService apiService =
                RetrofitClient.getClient(requireContext()).create(ApiService.class);

        Call<Integer> call = apiService.countCourseByStaus(status);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call,
                                   @NonNull Response<Integer> response) {

                if (!isAdded()) return;

                int count = 0;
                if (response.isSuccessful() && response.body() != null) {
                    count = response.body();
                }

                numberNewCourse = count;
                numberOfNewCourse.setText(String.valueOf(numberNewCourse));
                // ✅ CẬP NHẬT UI (VÍ DỤ)
            }

            @Override
            public void onFailure(@NonNull Call<Integer> call,
                                  @NonNull Throwable t) {

                if (!isAdded()) return;

                numberNewCourse = 0;
                numberOfNewCourse.setText(String.valueOf(numberNewCourse));
                Toast.makeText(
                        requireContext(),
                        "❌ Lỗi API: " + t.getMessage(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }


    private void loadCountNewTutor() {

        ApiService apiService =
                RetrofitClient.getClient(requireContext()).create(ApiService.class);

        Call<Integer> call = apiService.countNewTutor();

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call,
                                   @NonNull Response<Integer> response) {

                if (!isAdded()) return;

                int count = 0;
                if (response.isSuccessful() && response.body() != null) {
                    count = response.body();
                }

                numberNewTutor= count;
                numberOfNewTutor.setText(String.valueOf(numberNewTutor));

                // ✅ CẬP NHẬT UI (VÍ DỤ)
            }

            @Override
            public void onFailure(@NonNull Call<Integer> call,
                                  @NonNull Throwable t) {

                if (!isAdded()) return;

                numberNewTutor = 0;
                numberOfNewTutor.setText(String.valueOf(numberNewTutor));

                Toast.makeText(
                        requireContext(),
                        "❌ Lỗi API: " + t.getMessage(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
    private void loadCountNewStudent() {

        ApiService apiService =
                RetrofitClient.getClient(requireContext()).create(ApiService.class);

        Call<Integer> call = apiService.countNewStudent();

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call,
                                   @NonNull Response<Integer> response) {

                if (!isAdded()) return;

                int count = 0;
                if (response.isSuccessful() && response.body() != null) {
                    count = response.body();
                }

                numberNewStudent= count;
                numberOfNewStudent.setText(String.valueOf(numberNewStudent));

                // ✅ CẬP NHẬT UI (VÍ DỤ)
            }

            @Override
            public void onFailure(@NonNull Call<Integer> call,
                                  @NonNull Throwable t) {

                if (!isAdded()) return;

                numberNewStudent = 0;
                numberOfNewStudent.setText(String.valueOf(numberNewStudent));

                Toast.makeText(
                        requireContext(),
                        "❌ Lỗi API: " + t.getMessage(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
    private void loadCountSession() {

        ApiService apiService =
                RetrofitClient.getClient(requireContext()).create(ApiService.class);

        Call<List<SessionStatusCount>> call =
                apiService.countSessionAllStatus();

        call.enqueue(new Callback<List<SessionStatusCount>>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<SessionStatusCount>> call,
                    @NonNull Response<List<SessionStatusCount>> response) {

                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null) {

                    List<SessionStatusCount> list = response.body();

                    int completed = 0;
                    int unCompleted = 0;
                    int canceled = 0;

                    for (SessionStatusCount item : list) {

                        if ("COMPLETED".equals(item.getStatus())) {
                            completed = item.getTotal();
                        }

                        if ("SCHEDULED".equals(item.getStatus())) {
                            unCompleted = item.getTotal();
                        }

                        if ("CANCELED".equals(item.getStatus())) {
                            canceled = item.getTotal();
                        }
                    }

                    show(completed, unCompleted, canceled);

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
                    @NonNull Call<List<SessionStatusCount>> call,
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


}
