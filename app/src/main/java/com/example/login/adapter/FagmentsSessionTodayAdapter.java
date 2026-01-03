package com.example.login.adapter;

import com.example.login.model.SessionInfo;
import com.example.login.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class FagmentsSessionTodayAdapter extends ArrayAdapter<SessionInfo> {

    public FagmentsSessionTodayAdapter(Context context, List<SessionInfo> sessions) {
        super(context, R.layout.list_item_fragment_sessions_today, sessions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = convertView == null ? inflater.inflate(R.layout.list_item_fragment_sessions_today, parent, false) : convertView;

        SessionInfo session = getItem(position);

        TextView txtSubject = view.findViewById(R.id.txtSubject);
        TextView txtTutor = view.findViewById(R.id.txtTutor);
        TextView txtStatus = view.findViewById(R.id.txtStatus);
        TextView txtDuration = view.findViewById(R.id.txtDuration);
        TextView txtSessionDate = view.findViewById(R.id.txtSessionDate);

        if (session != null) {
            view.setTag(session.getId()); //getId()
            txtSubject.setText("📘 " + session.getSubject());
            txtTutor.setText("🎓 Học viên: " + session.getFullName());
            txtStatus.setText("⏳ Trạng thái: " + session.getStatus());
            txtSessionDate.setText("🗓️ Ngày học: " + session.getSessionDate());
            txtDuration.setText("Thời lượng: "+ session.getDuration());
        }

        return view;
    }
}
