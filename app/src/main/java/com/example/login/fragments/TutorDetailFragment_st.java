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

public class TutorDetailFragment_st extends Fragment {
    private static final String ARG_TUTOR_ID = "tutor_id";

    public static TutorDetailFragment_st newInstance(int tutorId) {
        TutorDetailFragment_st fragment = new TutorDetailFragment_st();
        Bundle args = new Bundle();
        args.putInt(ARG_TUTOR_ID, tutorId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tutor_detail_st, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView txtTutorId = view.findViewById(R.id.txtTutorId);
        if (getArguments() != null) {
            int tutorId = getArguments().getInt(ARG_TUTOR_ID);
            txtTutorId.setText("Tutor ID: " + tutorId);
        }
    }
}
