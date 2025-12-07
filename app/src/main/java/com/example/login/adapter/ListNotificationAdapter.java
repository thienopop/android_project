package com.example.login.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.login.R;
import com.example.login.model.Notification;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.login.R;
import com.example.login.model.SessionInfo;

import java.util.List;
public class ListNotificationAdapter extends ArrayAdapter<Notification> {

    // Constructor chuẩn
    public ListNotificationAdapter(Context context, List<Notification> notification) {
        super(context, R.layout.list_notification, notification);
    }

    /**
     * Lớp ViewHolder được sử dụng để lưu trữ các tham chiếu đến các View con,
     * tránh việc gọi findViewById() nhiều lần.
     */
    private static class ViewHolder {
        TextView txtNotificationDate,txtmessage,txtIsReaded,txtTitle;

//        android:id="@+id/txtNotificationDate"    android:id="@+id/txtmessage" android:id="@+id/txtIsReaded" android:id="@+id/txtTitle"
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        Notification notification= getItem(position);
        // 1. Kiểm tra convertView để tái sử dụng View
        if (convertView == null) {
            // View chưa được tạo, cần inflate layout và tạo ViewHolder mới
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_notification, parent, false);
            holder = new ViewHolder();
            // Ánh xạ View và lưu vào holder
            holder.txtTitle = convertView.findViewById(R.id.txtTitle);
            holder.txtIsReaded = convertView.findViewById(R.id.txtIsReaded);
            holder.txtmessage = convertView.findViewById(R.id.txtmessage);
            holder.txtNotificationDate = convertView.findViewById(R.id.txtNotificationDate);
            // Gắn holder vào View bằng setTag()
            convertView.setTag(holder);
        } else {
            // View đã tồn tại, lấy ViewHolder từ getTag()
            holder = (ViewHolder) convertView.getTag();
        }
        // 2. Gán dữ liệu (Đảm bảo dữ liệu được chuyển thành String)
        if (notification != null) {

            // Gắn ID của session vào View để xử lý sự kiện click ngoài Adapter nếu cần

            // Gán Ngày Bắt đầu (Giả định là String)
            holder.txtTitle.setText("Title : "+notification.getTitle());

            // Chuyển Duration (Integer/int) sang String
            // Có thể thêm đơn vị nếu cần: String.valueOf(session.getDuration()) + " phút"
            if(notification.getIsRead()==true)
            {
                holder.txtIsReaded .setText("Đã đọc");
            }
            else{
                holder.txtIsReaded .setText("Chưa đọc");
            }
            // Gán Trạng thái (String)
            holder.txtmessage.setText("Message: "+notification.getMessage());
            // Gán Trạng thái (String)
            holder.txtNotificationDate.setText(": "+notification.getCreatedAt());

        }

        return convertView;
    }
}