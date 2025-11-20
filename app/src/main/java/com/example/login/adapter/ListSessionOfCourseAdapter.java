package com.example.login.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.login.R;
import com.example.login.model.DetailCourse;

import java.util.List;

public class ListSessionOfCourseAdapter extends ArrayAdapter<DetailCourse> {

    public ListSessionOfCourseAdapter(Context context, List<DetailCourse> detailCourse) {
        super(context, R.layout.list_session_of_course, detailCourse);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = convertView == null ? inflater.inflate(R.layout.list_session_of_course, parent, false) : convertView;

        DetailCourse course = getItem(position);


        TextView text_timeOfTheLesson= view.findViewById(R.id.text_timeOfTheLesson);
        TextView text_complete_sessions= view.findViewById(R.id.text_complete_sessions);
        TextView text_notes= view.findViewById(R.id.text_notes);
        TextView text_status= view.findViewById(R.id.text_status);
        TextView text_end_date= view.findViewById(R.id.text_end_date);
        TextView text_start_date= view.findViewById(R.id.text_start_date);
        TextView text_total_price= view.findViewById(R.id.text_total_price);
        TextView text_total_sessions= view.findViewById(R.id.text_total_sessions);
        TextView text_subject= view.findViewById(R.id.text_subject);
        TextView text_full_name= view.findViewById(R.id.text_full_name);

        if (course != null) {
            view.setTag(course.getId()); // Gắn ID vào View để tái sử dụng
            text_complete_sessions.setText( course.getSubject());
            text_notes.setText(course.getSubject());
            text_timeOfTheLesson.setText( course.getSubject());
            text_full_name.setText(course.getSubject());

            text_end_date.setText(course.getSubject());
            text_subject.setText( course.getSubject());
            text_start_date.setText(course.getSubject());
            text_total_price.setText(course.getSubject());
            text_total_sessions.setText(course.getSubject());
            text_total_sessions.setText(course.getSubject());


             }
        return view;
    }
}
