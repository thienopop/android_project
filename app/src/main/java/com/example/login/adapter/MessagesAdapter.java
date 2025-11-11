package com.example.login.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.model.Message;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.VH> {

    private List<Message> messages;
    private int currentUserId;
    private OnAttachmentClickListener attachmentClickListener;

    public MessagesAdapter(List<Message> messages, int currentUserId) {
        this.messages = messages;
        this.currentUserId = currentUserId;
    }

    public void setMessages(List<Message> data) {
        this.messages = data;
        notifyDataSetChanged();
    }

    public void addMessage(Message m) {
        this.messages.add(m);
        notifyItemInserted(messages.size() - 1);
    }

    public void setOnAttachmentClickListener(OnAttachmentClickListener l) {
        this.attachmentClickListener = l;
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
        boolean isCurrentUser = msg.getSenderId() != null && msg.getSenderId().intValue() == currentUserId;
        boolean hasAttachment = msg.getAttachmentUrl() != null && !msg.getAttachmentUrl().isEmpty();

        holder.tvMessage.setText(msg.getMessageText());
        holder.tvTimestamp.setText(formatTimestamp(msg.getCreatedAt()));

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.messageRow.getLayoutParams();
        params.gravity = isCurrentUser ? Gravity.END : Gravity.START;
        holder.messageRow.setLayoutParams(params);

        int bubbleColor = isCurrentUser ? Color.parseColor("#DCF8C6") : Color.parseColor("#E0E0E0");
        holder.tvMessage.setBackgroundTintList(ColorStateList.valueOf(bubbleColor));

        holder.tvMessage.setTextColor(Color.BLACK);

        if (hasAttachment && attachmentClickListener != null) {
            holder.tvMessage.setTextColor(Color.DKGRAY);
            holder.tvMessage.setOnClickListener(v -> attachmentClickListener.onAttachmentClick(msg));
        } else {
            holder.tvMessage.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return messages == null ? 0 : messages.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        LinearLayout messageRow;
        TextView tvMessage, tvTimestamp;

        VH(@NonNull View itemView) {
            super(itemView);
            messageRow = itemView.findViewById(R.id.messageRow);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
        }
    }

    public interface OnAttachmentClickListener {
        void onAttachmentClick(Message message);
    }

    private String formatTimestamp(String iso) {
        if (iso == null || iso.isEmpty()) return "";
        try {
            LocalDateTime dt = LocalDateTime.parse(iso, DateTimeFormatter.ISO_DATE_TIME);
            return dt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        } catch (Exception e) {
            return iso;
        }
    }
}
