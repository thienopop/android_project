package com.example.login.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.login.R;
import com.example.login.model.SessionInfo;

import java.util.List;

public class ListSessionOfCourseAdapter extends ArrayAdapter<SessionInfo> {

    // Constructor chuẩn
    public ListSessionOfCourseAdapter(Context context, List<SessionInfo> sessionInfo) {
        super(context, R.layout.list_session_of_course, sessionInfo);
    }

    /**
     * Lớp ViewHolder được sử dụng để lưu trữ các tham chiếu đến các View con,
     * tránh việc gọi findViewById() nhiều lần.
     */
    private static class ViewHolder {
        TextView txtStatus;
        TextView txtStartDate;
        TextView txtDuration;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        SessionInfo session = getItem(position);

        // 1. Kiểm tra convertView để tái sử dụng View
        if (convertView == null) {
            // View chưa được tạo, cần inflate layout và tạo ViewHolder mới
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_session_of_course, parent, false);

            holder = new ViewHolder();
            // Ánh xạ View và lưu vào holder
            holder.txtStatus = convertView.findViewById(R.id.txtStatus);
            holder.txtStartDate = convertView.findViewById(R.id.txtStartDate);
            holder.txtDuration = convertView.findViewById(R.id.txtDuration);

            // Gắn holder vào View bằng setTag()
            convertView.setTag(holder);
        } else {
            // View đã tồn tại, lấy ViewHolder từ getTag()
            holder = (ViewHolder) convertView.getTag();
        }

        // 2. Gán dữ liệu (Đảm bảo dữ liệu được chuyển thành String)
        if (session != null) {

            // Gắn ID của session vào View để xử lý sự kiện click ngoài Adapter nếu cần

            // Gán Ngày Bắt đầu (Giả định là String)
            holder.txtStartDate.setText(session.getSessionDate());

            // Chuyển Duration (Integer/int) sang String
            // Có thể thêm đơn vị nếu cần: String.valueOf(session.getDuration()) + " phút"
            holder.txtDuration.setText(String.valueOf(session.getDuration()));

            // Gán Trạng thái (String)
            holder.txtStatus.setText(session.getStatus());
        }

        return convertView;
    }
}