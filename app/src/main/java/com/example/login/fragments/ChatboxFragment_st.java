package com.example.login.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.adapter.MessagesAdapter_st;
import com.example.login.api.ApiService;
import com.example.login.api.PrefsHelper;
import com.example.login.api.RetrofitClient;
import com.example.login.model.UploadFileResponse;
import com.example.login.model.Message;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatboxFragment_st extends Fragment implements MessagesAdapter_st.OnAttachmentClickListener {

    private static final int REQUEST_CODE_PICK_FILES = 1234;

    private int chatId, chatUserId;
    private String chatUsername, chatFullName;
    private TextView tvName, tvUsername;
    private RecyclerView rvMessages;
    private EditText etMessage;
    private ImageButton btnBack, btnSend, btnAttach;
    private MessagesAdapter_st adapter;
    private com.example.login.api.StompClient stompClient;
    private Gson gson = new Gson();

    public static ChatboxFragment_st newInstance(int chatId, int chatUserId, String username, String fullName) {
        ChatboxFragment_st f = new ChatboxFragment_st();
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
        btnAttach = v.findViewById(R.id.btnAttach);

        tvName.setText(chatFullName != null ? chatFullName : "(no name)");
        tvUsername.setText(chatUsername != null ? "@" + chatUsername : "");

        rvMessages.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MessagesAdapter_st(new ArrayList<>(), chatUserId);
        adapter.setOnAttachmentClickListener(this);
        rvMessages.setAdapter(adapter);

        loadMessages();
        setupWebSocket();

        btnBack.setOnClickListener(x -> {
            if (getActivity() != null) getActivity().getSupportFragmentManager().popBackStack();
        });
        btnSend.setOnClickListener(x -> sendText());
        btnAttach.setOnClickListener(x -> openFilePicker());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            if (stompClient != null) stompClient.disconnect();
        } catch (Exception ignored) {}
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
        m.setSenderId(PrefsHelper.getUserId(getContext()));
        m.setMessageText(text);
        m.setAttachmentUrl(null);
        if (stompClient != null) stompClient.send("/app/chats/" + chatId, gson.toJson(m));
        adapter.addMessage(m);
        etMessage.setText("");
        rvMessages.scrollToPosition(adapter.getItemCount() - 1);
    }

    private void openFilePicker() {
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(i, REQUEST_CODE_PICK_FILES);
    }

    @Override
    public void onActivityResult(int req, int res, @Nullable Intent data) {
        super.onActivityResult(req, res, data);
        if (req == REQUEST_CODE_PICK_FILES && res == Activity.RESULT_OK && data != null) {
            List<Uri> uris = new ArrayList<>();
            if (data.getData() != null) uris.add(data.getData());
            else if (data.getClipData() != null)
                for (int i = 0; i < data.getClipData().getItemCount(); i++)
                    uris.add(data.getClipData().getItemAt(i).getUri());
            if (!uris.isEmpty()) confirmAndUpload(uris);
        }
    }

    private void confirmAndUpload(List<Uri> uris) {
        new AlertDialog.Builder(getContext())
                .setTitle("Gửi file")
                .setMessage("Gửi " + uris.size() + " file?")
                .setPositiveButton("Gửi", (d, w) -> uploadFiles(uris))
                .setNegativeButton("Huỷ", null)
                .show();
    }

    private void uploadFiles(List<Uri> uris) {
        ApiService api = RetrofitClient.getClient(getContext()).create(ApiService.class);
        new Thread(() -> {
            for (Uri uri : uris) {
                try {
                    File file = toFile(uri);
                    if (file == null) continue;
                    RequestBody req = RequestBody.create(MediaType.parse(getContext().getContentResolver().getType(uri)), file);
                    MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), req);
                    Response<UploadFileResponse> resp = api.uploadFile(part).execute();
                    if (resp.isSuccessful() && resp.body() != null) {
                        UploadFileResponse fur = resp.body();
                        Message m = new Message();
                        m.setSenderId(PrefsHelper.getUserId(getContext()));
                        m.setAttachmentUrl(fur.getFileUrl());
                        m.setMessageText("đã gửi file " + fur.getFileName());
                        if (getActivity() != null)
                            getActivity().runOnUiThread(() -> {
                                adapter.addMessage(m);
                                rvMessages.scrollToPosition(adapter.getItemCount() - 1);
                            });
                        if (stompClient != null) stompClient.send("/app/chats/" + chatId, gson.toJson(m));
                    }
                } catch (Exception e) {
                    Log.e("ChatboxFragment", "upload error", e);
                }
            }
        }).start();
    }

    private File toFile(Uri uri) {
        try {
            String name = queryName(getContext().getContentResolver(), uri);
            InputStream is = getContext().getContentResolver().openInputStream(uri);
            File f = new File(getContext().getCacheDir(), name);
            try (OutputStream os = new FileOutputStream(f)) {
                byte[] buf = new byte[4096];
                int len;
                while ((len = is.read(buf)) > 0) os.write(buf, 0, len);
            }
            return f;
        } catch (Exception e) {
            return null;
        }
    }

    private String queryName(ContentResolver resolver, Uri uri) {
        Cursor c = resolver.query(uri, null, null, null, null);
        if (c != null) {
            int idx = c.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            c.moveToFirst();
            String name = c.getString(idx);
            c.close();
            return name;
        }
        return "file";
    }

    @Override
    public void onAttachmentClick(Message m) {
        if (m.getAttachmentUrl() == null) return;
        String url = m.getAttachmentUrl();
        String name = url.substring(url.lastIndexOf('/') + 1);
        ApiService api = RetrofitClient.getClient(getContext()).create(ApiService.class);
        api.downloadFile(name).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> res) {
                if (res.isSuccessful() && res.body() != null) {
                    boolean saved = saveToDownloads(res.body(), name);
                    Toast.makeText(getContext(), saved ? "Đã tải xuống" : "Lưu thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi tải xuống", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean saveToDownloads(ResponseBody body, String filename) {
        try {
            File downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!downloads.exists()) downloads.mkdirs();
            File out = new File(downloads, filename);
            try (InputStream is = body.byteStream(); FileOutputStream fos = new FileOutputStream(out)) {
                byte[] buf = new byte[4096];
                int len;
                while ((len = is.read(buf)) != -1) fos.write(buf, 0, len);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
