package com.example.login.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.model.ChatWithUserDetail;

import java.util.ArrayList;
import java.util.List;

public class ChatsAdapter_st extends RecyclerView.Adapter<ChatsAdapter_st.VH> {

    private List<ChatWithUserDetail> items;
    private List<ChatWithUserDetail> original;
    private OnItemClickListener listener;

    public ChatsAdapter_st(List<ChatWithUserDetail> items) {
        this.items = items;
        this.original = new ArrayList<>(items);
    }

    public void setItems(List<ChatWithUserDetail> data) {
        this.items = data;
        this.original = new ArrayList<>(data);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        this.listener = l;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        ChatWithUserDetail item = items.get(position);
        holder.tvName.setText(item.getFullName() == null ? "(no name)" : item.getFullName());
        holder.tvUsername.setText(item.getUsername() == null ? "" : item.getUsername());
        holder.tvUnreadDot.setVisibility(
                Boolean.TRUE.equals(item.getHasUnreadMessages()) ? View.VISIBLE : View.GONE
        );
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public void filter(String query) {
        if (query == null) query = "";
        query = query.trim().toLowerCase();
        if (query.isEmpty()) {
            items = new ArrayList<>(original);
        } else {
            List<ChatWithUserDetail> filtered = new ArrayList<>();
            for (ChatWithUserDetail c : original) {
                String name = c.getFullName() == null ? "" : c.getFullName().toLowerCase();
                String username = c.getUsername() == null ? "" : c.getUsername().toLowerCase();
                if (name.contains(query) || username.contains(query)) filtered.add(c);
            }
            items = filtered;
        }
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvUsername, tvUnreadDot;
        ImageView ivAvatar;

        VH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvChatName);
            tvUsername = itemView.findViewById(R.id.tvChatUsername);
            tvUnreadDot = itemView.findViewById(R.id.tvChatUnreadDot);
            ivAvatar = itemView.findViewById(R.id.ivChatAvatar);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ChatWithUserDetail item);
    }
}