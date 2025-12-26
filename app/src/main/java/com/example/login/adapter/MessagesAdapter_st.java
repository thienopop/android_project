package com.example.login.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.model.Message;
import com.example.login.api.PrefsHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessagesAdapter_st extends RecyclerView.Adapter<MessagesAdapter_st.VH> {
    private Context context;
    private List<Message> messages;
    private OnMessageClickListener listener;

    public interface OnMessageClickListener {
        void onMessageClick(Message message);
    }

    public MessagesAdapter_st(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    public void setOnMessageClickListener(OnMessageClickListener listener) {
        this.listener = listener;
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
        boolean hasAttachment = msg.getAttachmentUrl() != null && !msg.getAttachmentUrl().isEmpty();

        if (hasAttachment) {
            String cleanFileName = removeUniqueTimestamp(msg.getAttachmentUrl());
            String displayText = "Đã đính kèm tệp " + cleanFileName;
            SpannableString spannableString = new SpannableString(displayText);
            spannableString.setSpan(new StyleSpan(Typeface.ITALIC), 0, displayText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tvMessage.setText(spannableString);
            holder.ivDownload.setVisibility(View.VISIBLE);
        } else {
            holder.tvMessage.setText(msg.getMessageText());
            holder.ivDownload.setVisibility(View.GONE);
        }

        holder.tvTimestamp.setText(formatTimestamp(msg.getCreatedAt()));

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.tvMessage.getLayoutParams();
        ConstraintLayout.LayoutParams iconParams = (ConstraintLayout.LayoutParams) holder.ivDownload.getLayoutParams();

        if (isCurrentUser) {
            params.startToStart = ConstraintLayout.LayoutParams.UNSET;
            params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            iconParams.startToEnd = ConstraintLayout.LayoutParams.UNSET;
            iconParams.endToStart = holder.tvMessage.getId();
        } else {
            params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            params.endToEnd = ConstraintLayout.LayoutParams.UNSET;
            iconParams.startToEnd = holder.tvMessage.getId();
            iconParams.endToStart = ConstraintLayout.LayoutParams.UNSET;
        }
        holder.tvMessage.setLayoutParams(params);
        holder.ivDownload.setLayoutParams(iconParams);

        ConstraintLayout.LayoutParams tsParams = (ConstraintLayout.LayoutParams) holder.tvTimestamp.getLayoutParams();
        tsParams.startToStart = params.startToStart;
        tsParams.endToEnd = params.endToEnd;
        holder.tvTimestamp.setLayoutParams(tsParams);

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int maxWidth = (int) (metrics.widthPixels * 0.8);
        holder.tvMessage.setMaxWidth(maxWidth);

        int bubbleColor = isCurrentUser ? Color.parseColor("#03A9F4") : Color.parseColor("#E0E0E0");
        holder.tvMessage.setBackgroundTintList(ColorStateList.valueOf(bubbleColor));

        holder.itemView.setOnClickListener(v -> {
            if (hasAttachment && listener != null) {
                listener.onMessageClick(msg);
            }
        });
    }

    @Override
    public int getItemCount() {
        return messages == null ? 0 : messages.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTimestamp;
        ImageView ivDownload;

        VH(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            ivDownload = itemView.findViewById(R.id.ivDownload);
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

    private String removeUniqueTimestamp(String fileName) {
        if (fileName == null || fileName.isEmpty()) return fileName;

        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) return fileName;

        String nameWithoutExt = fileName.substring(0, lastDotIndex);
        String extension = fileName.substring(lastDotIndex);

        int lastUnderscoreIndex = nameWithoutExt.lastIndexOf('_');
        if (lastUnderscoreIndex == -1) return fileName;

        String timestampPart = nameWithoutExt.substring(lastUnderscoreIndex + 1);
        if (timestampPart.matches("\\d+")) {
            String originalName = nameWithoutExt.substring(0, lastUnderscoreIndex);
            return originalName + extension;
        }

        return fileName;
    }
}