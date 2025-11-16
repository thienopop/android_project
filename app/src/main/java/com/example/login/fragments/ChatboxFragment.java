package com.example.login.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.adapter.MessagesAdapter;
import com.example.login.api.ApiService;
import com.example.login.api.PrefsHelper;
import com.example.login.api.RetrofitClient;
import com.example.login.model.Message;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatboxFragment extends Fragment {

    private int chatId, chatUserId;
    private String chatUsername, chatFullName;
    private TextView tvName, tvUsername;
    private RecyclerView rvMessages;
    private EditText etMessage;
    private ImageButton btnBack, btnSend;
    private MessagesAdapter adapter;
    private com.example.login.api.StompClient stompClient;
    private Gson gson = new Gson();

    public static ChatboxFragment newInstance(int chatId, int chatUserId, String username, String fullName) {
        ChatboxFragment f = new ChatboxFragment();
        Bundle b = new Bundle();
        b.putInt("chatId", chatId);
        b.putInt("chatUserId", chatUserId);
        b.putString("chatUsername", username);
        b.putString("chatFullName", fullName);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chatbox, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        if (getArguments() != null) {
            chatId = getArguments().getInt("chatId", -1);
            chatUserId = getArguments().getInt("chatUserId", -1);
            chatUsername = getArguments().getString("chatUsername");
            chatFullName = getArguments().getString("chatFullName");
        }

        tvName = v.findViewById(R.id.tvChatTopName);
        tvUsername = v.findViewById(R.id.tvChatTopUsername);
        rvMessages = v.findViewById(R.id.recyclerViewMessages);
        etMessage = v.findViewById(R.id.etMessage);
        btnBack = v.findViewById(R.id.btnBack);
        btnSend = v.findViewById(R.id.btnSend);

        tvName.setText(chatFullName != null ? chatFullName : "(no name)");
        tvUsername.setText(chatUsername != null ? "@" + chatUsername : "");

        rvMessages.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MessagesAdapter(getContext(), new ArrayList<>());
        rvMessages.setAdapter(adapter);

        loadMessages();
        setupWebSocket();

        btnBack.setOnClickListener(x -> {
            if (getActivity() != null) getActivity().getSupportFragmentManager().popBackStack();
        });
        btnSend.setOnClickListener(x -> sendText());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            if (stompClient != null) stompClient.disconnect();
        } catch (Exception ignored) {
        }
    }

    private void loadMessages() {
        ApiService api = RetrofitClient.getClient(getContext()).create(ApiService.class);
        api.getChatMessages(chatId).enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> res) {
                if (res.isSuccessful() && res.body() != null) {
                    Log.d("ChatboxFragment", "loaded " + res.body().size() + " messages");
                    Log.d("ChatboxFragment", "messages: " + gson.toJson(res.body()));
                    adapter.setMessages(res.body());
                    rvMessages.scrollToPosition(adapter.getItemCount() - 1);
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Toast.makeText(getContext(), "Không thể tải tin nhắn", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupWebSocket() {
        stompClient = new com.example.login.api.StompClient(RetrofitClient.getWebSocketEndpoint());
        stompClient.connect();
        stompClient.subscribe("/topic/chats/" + chatId, (body) -> {
            if (getActivity() == null) return;
            try {
                Message m = gson.fromJson(body, Message.class);
                getActivity().runOnUiThread(() -> {
                    adapter.addMessage(m);
                    rvMessages.scrollToPosition(adapter.getItemCount() - 1);
                });
            } catch (Exception e) {
                Log.e("ChatboxFragment", "invalid stomp msg", e);
            }
        });
    }

    private void sendText() {
        String text = etMessage.getText().toString().trim();
        if (text.isEmpty()) return;

        Message m = new Message();
        m.setSenderId(PrefsHelper.getCurrentUserId(getContext()));
        m.setMessageText(text);
        m.setAttachmentUrl(null);

        if (stompClient != null) stompClient.send("/app/chats/" + chatId, gson.toJson(m));

        etMessage.setText("");
    }
}
