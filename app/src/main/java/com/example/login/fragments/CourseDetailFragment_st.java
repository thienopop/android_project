package com.example.login.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.login.R;

public class CourseDetailFragment_st extends Fragment {
    private static final String ARG_COURSE_ID = "course_id";

    public static CourseDetailFragment_st newInstance(int courseId) {
        CourseDetailFragment_st fragment = new CourseDetailFragment_st();
        Bundle args = new Bundle();
        args.putInt(ARG_COURSE_ID, courseId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_detail_st, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView txtCourseId = view.findViewById(R.id.txtCourseId);
        if (getArguments() != null) {
            int courseId = getArguments().getInt(ARG_COURSE_ID);
            txtCourseId.setText("Course ID: " + courseId);
        }
    }
}