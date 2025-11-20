package com.example.login.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.api.PrefsHelper;
import com.example.login.model.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.VH> {

    private Context context;
    private List<Message> messages;

    public MessagesAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    public void setMessages(List<Message> data) {
        this.messages = data;
        notifyDataSetChanged();
    }

    public void addMessage(Message m) {
        this.messages.add(m);
        notifyItemInserted(messages.size() - 1);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Message msg = messages.get(position);
        int currentUserId = PrefsHelper.getCurrentUserId(context);
        boolean isCurrentUser = msg.getSenderId() != null && msg.getSenderId() == currentUserId;

        holder.tvMessage.setText(msg.getMessageText());
        holder.tvTimestamp.setText(formatTimestamp(msg.getCreatedAt()));

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.tvMessage.getLayoutParams();
        if (isCurrentUser) {
            params.startToStart = ConstraintLayout.LayoutParams.UNSET;
            params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        } else {
            params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            params.endToEnd = ConstraintLayout.LayoutParams.UNSET;
        }
        holder.tvMessage.setLayoutParams(params);

        ConstraintLayout.LayoutParams tsParams = (ConstraintLayout.LayoutParams) holder.tvTimestamp.getLayoutParams();
        tsParams.startToStart = params.startToStart;
        tsParams.endToEnd = params.endToEnd;
        holder.tvTimestamp.setLayoutParams(tsParams);

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int maxWidth = (int) (metrics.widthPixels * 0.8);
        holder.tvMessage.setMaxWidth(maxWidth);

        int bubbleColor = isCurrentUser ? Color.parseColor("#03A9F4") : Color.parseColor("#E0E0E0");
        holder.tvMessage.setBackgroundTintList(ColorStateList.valueOf(bubbleColor));
        holder.tvMessage.setTextColor(Color.BLACK);
    }

    @Override
    public int getItemCount() {
        return messages == null ? 0 : messages.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTimestamp;

        VH(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
        }
    }

    private String formatTimestamp(String iso) {
        if (iso == null || iso.isEmpty()) return "";
        try {
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date date = input.parse(iso);

            SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            return output.format(date);
        } catch (Exception e) {
            return iso;
        }
    }

}
