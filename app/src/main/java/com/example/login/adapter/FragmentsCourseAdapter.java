package com.example.login.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.login.R;
import com.example.login.model.CourseInfo;

import java.util.List;

public class FragmentsCourseAdapter extends ArrayAdapter<CourseInfo> {

    public FragmentsCourseAdapter(Context context, List<CourseInfo> courseList) {
        super(context, R.layout.list_item_fragment_course, courseList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = convertView == null ? inflater.inflate(R.layout.list_item_fragment_course, parent, false) : convertView;

        CourseInfo course = getItem(position);



        TextView txtSubject = view.findViewById(R.id.txtSubject);
        TextView txtStudent = view.findViewById(R.id.txtStudent);
        TextView txtStatus = view.findViewById(R.id.txtStatus);
        TextView txtTotalSessions = view.findViewById(R.id.txtTotalSessions);
        TextView txtStartDate = view.findViewById(R.id.txtStartDate);


        if (course != null) {
            view.setTag(course.getId()); // Gắn ID vào View để tái sử dụng
            txtSubject.setText("📘 " + course.getSubject());
            txtStudent.setText("🎓 Học viên: " + course.getFullName());
            txtStatus.setText("⏳ Trạng thái: " + course.getStatus());
            txtStartDate.setText("🗓️ Ngày học: " + course.getStartTime());
            txtTotalSessions.setText("⏱️ Buổi học đã hoàn thành: " + course.getSessionCompleted()+"/"+ course.getTotalSessions());
        }
        return view;
    }
}
