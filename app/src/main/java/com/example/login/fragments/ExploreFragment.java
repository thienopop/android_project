package com.example.login.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.login.R;

public class ExploreFragment extends Fragment {

    private LinearLayout layoutFilter;
    private ImageView btnFilter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutFilter = view.findViewById(R.id.layoutFilter);
        btnFilter = view.findViewById(R.id.btnFilter);

        btnFilter.setOnClickListener(v -> toggleFilterVisibility());
    }


    private void toggleFilterVisibility() {
        if (layoutFilter.getVisibility() == View.GONE) {
            layoutFilter.setVisibility(View.VISIBLE);
        } else {
            layoutFilter.setVisibility(View.GONE);
        }
    }
}
