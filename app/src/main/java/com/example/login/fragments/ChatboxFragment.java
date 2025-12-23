package com.example.login.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.adapter.MessagesAdapter;
import com.example.login.api.ApiService;
import com.example.login.api.PrefsHelper;
import com.example.login.api.RetrofitClient;
import com.example.login.model.Message;
import com.example.login.model.UploadFileResponse;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatboxFragment extends Fragment {
    private int chatId, chatUserId;
    private String chatUsername, chatFullName;
    private TextView tvName, tvUsername;
    private RecyclerView rvMessages;
    private EditText etMessage;
    private ImageButton btnBack, btnSend, btnAttach;
    private MessagesAdapter adapter;
    private com.example.login.api.StompClient stompClient;
    private Gson gson = new Gson();

    private ActivityResultLauncher<Intent> filePickerLauncher;
    private ActivityResultLauncher<String> permissionLauncher;
    private Message pendingDownloadMessage;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        filePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri fileUri = result.getData().getData();
                if (fileUri != null) {
                    uploadFile(fileUri);
                }
            }
        });

        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted && pendingDownloadMessage != null) {
                downloadFile(pendingDownloadMessage);
                pendingDownloadMessage = null;
            } else {
                Toast.makeText(getContext(), "Cần cấp quyền để tải xuống file", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chatbox, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            chatId = getArguments().getInt("chatId", -1);
            chatUserId = getArguments().getInt("chatUserId", -1);
            chatUsername = getArguments().getString("chatUsername");
            chatFullName = getArguments().getString("chatFullName");
        }

        tvName = view.findViewById(R.id.tvChatTopName);
        tvUsername = view.findViewById(R.id.tvChatTopUsername);
        rvMessages = view.findViewById(R.id.recyclerViewMessages);
        etMessage = view.findViewById(R.id.etMessage);
        btnBack = view.findViewById(R.id.btnBack);
        btnSend = view.findViewById(R.id.btnSend);
        btnAttach = view.findViewById(R.id.btnAttach);

        tvName.setText(chatFullName != null ? chatFullName : "(no name)");
        tvUsername.setText(chatUsername != null ? "@" + chatUsername : "");

        rvMessages.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MessagesAdapter(getContext(), new ArrayList<>());
        adapter.setOnMessageClickListener(this::downloadFile);
        rvMessages.setAdapter(adapter);

        loadMessages();
        setupWebSocket();

        btnBack.setOnClickListener(v -> {
            if (getActivity() != null) getActivity().getSupportFragmentManager().popBackStack();
        });
        btnSend.setOnClickListener(v -> sendText());
        btnAttach.setOnClickListener(v -> openFilePicker());
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

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        filePickerLauncher.launch(Intent.createChooser(intent, "Chọn tệp"));
    }

    private void uploadFile(Uri fileUri) {
        try {
            ContentResolver resolver = getContext().getContentResolver();
            String fileName = getFileName(fileUri);
            InputStream inputStream = resolver.openInputStream(fileUri);

            File tempFile = new File(getContext().getCacheDir(), fileName);
            FileOutputStream outputStream = new FileOutputStream(tempFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            inputStream.close();

            RequestBody requestFile = RequestBody.create(MediaType.parse(resolver.getType(fileUri)), tempFile);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", fileName, requestFile);

            ApiService api = RetrofitClient.getClient(getContext()).create(ApiService.class);
            api.uploadFile(body).enqueue(new Callback<UploadFileResponse>() {
                @Override
                public void onResponse(Call<UploadFileResponse> call, Response<UploadFileResponse> response) {
                    tempFile.delete();
                    if (response.isSuccessful() && response.body() != null) {
                        String uniqueFileName = response.body().getFileName();
                        sendFileMessage(uniqueFileName);
                    } else {
                        Toast.makeText(getContext(), "Tải lên thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UploadFileResponse> call, Throwable t) {
                    tempFile.delete();
                    Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(getContext(), "Lỗi đọc file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex >= 0) {
                        result = cursor.getString(nameIndex);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void sendFileMessage(String uniqueFileName) {
        Message m = new Message();
        m.setSenderId(PrefsHelper.getCurrentUserId(getContext()));
        m.setMessageText("");
        m.setAttachmentUrl(uniqueFileName);
        if (stompClient != null) stompClient.send("/app/chats/" + chatId, gson.toJson(m));
    }

    private void downloadFile(Message message) {
        if (message.getAttachmentUrl() == null || message.getAttachmentUrl().isEmpty()) {
            return;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                pendingDownloadMessage = message;
                permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                return;
            }
        }

        ApiService api = RetrofitClient.getClient(getContext()).create(ApiService.class);
        api.downloadFile(message.getAttachmentUrl()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    saveFile(response.body(), message.getAttachmentUrl());
                } else {
                    Toast.makeText(getContext(), "Tải xuống thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi tải xuống: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveFile(ResponseBody body, String fileName) {
        try {
            File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(downloadDir, fileName);

            InputStream inputStream = body.byteStream();
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            inputStream.close();

            Toast.makeText(getContext(), "Đã tải xuống: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Lỗi lưu file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}