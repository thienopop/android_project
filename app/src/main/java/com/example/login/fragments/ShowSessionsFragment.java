package com.example.login.fragments;

// Import đã được dọn dẹp và thêm các thứ cần thiết
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.DetailCourseActivity;
import com.example.login.R;
import com.example.login.api.ApiService;
import com.example.login.api.RetrofitClient;
import com.example.login.model.CourseIdCallback;
import com.example.login.model.CourseInfo;
import com.example.login.model.SessionInfo;
import com.example.login.adapter.FagmentsSessionTodayAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowSessionsFragment extends Fragment {

    private ListView listViewSessions;
    private EditText editTextDate;
    private Calendar myCalendar;
    private TextView txtMessage;

    // TÁCH RA: Định dạng này chỉ DÙNG ĐỂ HIỂN THỊ cho người dùng
    private SimpleDateFormat uiSdf;

    // TÁCH RA: Định dạng này DÙNG ĐỂ GỌI API (phải khớp với backend)
    private SimpleDateFormat apiSdf;

    public ShowSessionsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sessions_today, container, false);
        listViewSessions = view.findViewById(R.id.listViewSessions);
        editTextDate = view.findViewById(R.id.editTextDate);

        txtMessage = view.findViewById(R.id.txtMessageSh);
        myCalendar = Calendar.getInstance(); // Khởi tạo calendar (mặc định là hôm nay)

        // Khởi tạo các định dạng (format)
        String uiFormat = "dd/MM/yyyy"; // Hiển thị: 09/11/2025
        uiSdf = new SimpleDateFormat(uiFormat, Locale.US);

        String apiFormat = "yyyy-MM-dd"; // Gửi cho API: 2025-11-09
        apiSdf = new SimpleDateFormat(apiFormat, Locale.US);


        // LOGIC MỚI: Định nghĩa listener ở đây
        DatePickerDialog.OnDateSetListener dateSetListener = (view1, year, month, dayOfMonth) -> {
            // Cập nhật calendar với ngày người dùng chọn
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            // Cập nhật text cho EditText (dùng format UI)
            updateLabel();

            // FIX 4 & 5: Gọi loadSessions TỪ ĐÂY với format API
            String apiDateString = apiSdf.format(myCalendar.getTime());
            loadSessions(apiDateString);
        };

        // Gán sự kiện click cho EditText
        editTextDate.setOnClickListener(v -> {
            int year = myCalendar.get(Calendar.YEAR);
            int month = myCalendar.get(Calendar.MONTH);
            int day = myCalendar.get(Calendar.DAY_OF_MONTH);

            // FIX 1: Dùng getContext() thay vì 'this'
            //  dùng requireContext() sẽ an toàn hơn, tránh NullPointerException
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                    dateSetListener,
                    year, month, day);
            datePickerDialog.show();

            //  FIX 2 & 3: Đã XÓA các dòng code lỗi ở đây
        });


        //  KHỞI ĐỘNG: Load dữ liệu cho ngày hôm nay khi Fragment mới mở

        // 1. Cập nhật EditText để hiển thị ngày hôm nay (Format UI)
        updateLabel();

        // 2. Lấy chuỗi ngày hôm nay (Format API)
        String homNayApiFormat = apiSdf.format(myCalendar.getTime());

        // 3. Tải dữ liệu
        loadSessions(homNayApiFormat);

        return view;
    }

    private void loadSessions(String date) {
        // Kiểm tra context trước khi gọi API
        if (getContext() == null) {
            return;
        }

        ApiService apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);
        Call<List<SessionInfo>> call = apiService.getSessionsByTutor(date);

        call.enqueue(new Callback<List<SessionInfo>>() {
            @Override
            public void onResponse(@NonNull Call<List<SessionInfo>> call, @NonNull Response<List<SessionInfo>> response) {
                if (!isAdded()) return; // Đảm bảo fragment vẫn còn attached

                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    List<SessionInfo> sessionList = response.body();
                    txtMessage.setText("");
                    FagmentsSessionTodayAdapter adapter = new FagmentsSessionTodayAdapter(getContext(), sessionList);
                    listViewSessions.setAdapter(adapter);

                    listViewSessions.setOnItemClickListener((parent, view, position, id) -> {
                        SessionInfo ss = sessionList.get(position);
                        int sessionId= ss.getId();
                        loadCourseId(sessionId, courseId -> {

                            Intent intent = new Intent(requireContext(), DetailCourseActivity.class);

                            intent.putExtra("COURSE_ID_KEY", courseId);
                            startActivity(intent);

                        });
                    });
                } else {
                    // Xóa list cũ nếu không có dữ liệu mới
                    listViewSessions.setAdapter(null);
                    txtMessage.setText("Không có lịch học cho ngày :" + date);
//                    Toast.makeText(getContext(), "Không có lịch học cho ngày " + date, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<SessionInfo>> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(getContext(), "Lỗi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hàm này giờ chỉ làm 1 việc: Cập nhật EditText
    private void updateLabel() {
        editTextDate.setText(uiSdf.format(myCalendar.getTime()));
    }

    private void loadCourseId(int sessionId, CourseIdCallback callback) {

        ApiService apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);
        Call<Integer> call = apiService.findCourseIdBySessionId(sessionId);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(response.body());
                } else {
                    callback.onResult(0);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
                if (!isAdded()) return;

                Toast.makeText(getContext(), "Lỗi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                callback.onResult(0);
            }
        });
    }
}