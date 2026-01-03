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


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.login.R;
import com.example.login.model.CourseInfo;

import java.util.List;

public class ManagermentCourseAdapter_admin  extends ArrayAdapter<CourseInfo> {

    public ManagermentCourseAdapter_admin(Context context, List<CourseInfo> courseList) {
        super(context, R.layout.list_item_fragment_course_admin, courseList);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = convertView == null ? inflater.inflate(R.layout.list_item_fragment_course_admin, parent, false) : convertView;
        CourseInfo course = getItem(position);
        TextView txtSubject = view.findViewById(R.id.txtSubject);
        TextView txtTutor = view.findViewById(R.id.txtTutor);
        if (course != null) {
            view.setTag(course.getId()); // Gắn ID vào View để tái sử dụng
            txtSubject.setText("📘 " + course.getSubject());
            txtTutor.setText("🎓 Gia sư: " + course.getFullName());
        }
        return view;
    }
}
