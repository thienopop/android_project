package com.example.login.fragments;

import android.os.Bundle;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.login.R;
import com.example.login.api.ApiService;
import com.example.login.api.RetrofitClient;
import com.example.login.model.Notification;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.login.adapter. ListNotificationAdapter;

import java.util.List;


public class NotificationFragment   extends Fragment {

    private ListView listViewNotification;
    private Notification itemNotification;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.notification_fragment, container, false);
        listViewNotification = view.findViewById(R.id.listViewNotification);

        loadNotification();

        return view; // 👈 BẮT BUỘC PHẢI CÓ
    }


    private void loadNotification() {
        // Kiểm tra context trước khi gọi API
        if (getContext() == null) {
            return;
        }

        ApiService apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);
        Call<List<Notification>> call = apiService.getNotificationByUser();

        call.enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(@NonNull Call<List<Notification>> call, @NonNull Response<List<Notification>> response) {
                if (!isAdded()) return; // Đảm bảo fragment vẫn còn attached

                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    List<Notification> notification = response.body();
//                    txtMessage.setText("");
                    ListNotificationAdapter adapter = new ListNotificationAdapter(getContext(), notification);
                    listViewNotification.setAdapter(adapter);

                    listViewNotification.setOnItemClickListener((parent, view, position, id) -> {
                        int notiId = (int) view.getTag(R.id.backGround);
                        Toast.makeText(getContext(), "Bạn đã chọn " + notiId, Toast.LENGTH_SHORT).show();
//                        loadNoti(notiId);
                        Notification noti=loadNotification( notification, notiId);
                        openPopup(noti);
//                        cập nhật trạng thái đã đọc
                        if(noti.getIsRead()!=true)
                        {
                            updateIsRead(noti.getId());

                        }


//

                    });

                } else {
                    // Xóa list cũ nếu không có dữ liệu mới
                    listViewNotification.setAdapter(null);
//                    txtMessage.setText("Không có lịch học cho ngày :" + date);
//                    Toast.makeText(getContext(), "Không có lịch học cho ngày " + date, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Notification>> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(getContext(), "❌ Lỗi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


//
//    public void loadNoti(int noti_id) {
//
//        ApiService apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);
//        Call<Notification> call = apiService.getNotificationById(noti_id);
////            Call<Notification> getNotificationById(@Body int Id);
//        call.enqueue(new Callback<Notification>() {
//            @Override
//            public void onResponse(@NonNull Call<Notification> call, @NonNull Response<Notification> response) {
//                if (!isAdded()) return;
//
//                if (response.isSuccessful() && response.body() != null) {
//
//                    itemNotification = response.body();
//
//                    // 👉 Mở popup NGAY TẠI ĐÂY
//                    openPopup(itemNotification);
//
//                } else {
//                    Toast.makeText(getContext(), "Không có dữ liệu", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<Notification> call, @NonNull Throwable t) {
//                if (!isAdded()) return;
//                Toast.makeText(getContext(), "Lỗi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//

    private Notification loadNotification(List<Notification> notifications, int id) {
        for (Notification noti : notifications) {
            if (noti.getId() == id) {
                return noti; // tìm thấy thì trả về luôn
            }
        }
        return null; // không tìm thấy
    }


    private void openPopup(Notification noti) {

        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.show_detail_notification_card);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(true);

        TextView txtPopupTitle = dialog.findViewById(R.id.txtPopupTitle);
        TextView txtPopupMessage = dialog.findViewById(R.id.txtPopupMessage);

        txtPopupTitle.setText(noti.getTitle());
        txtPopupMessage.setText(noti.getMessage());

        Button btnClose = dialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(v -> dialog.dismiss());
        dialog.show();


    }




    public void updateIsRead(int noti_id) {

        ApiService apiService = RetrofitClient.getClient(getContext()).create(ApiService.class);
        Call<Void> call = apiService.updateIsReadNotificationById(noti_id);
//            Call<Notification> getNotificationById(@PATH int Id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!isAdded()) return;

            }
            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(getContext(), "Lỗi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




}