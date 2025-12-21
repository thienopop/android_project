
package com.example.login.api;

import android.content.Context;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    // URL dùng để chạy trên thiết bị giả lập Android
    private static final String BASE_URL_EMULATOR = "http://10.0.2.2:8080/";
    // URL dùng để chạy trên thiết bị Android thật
    private static final String BASE_URL_PHYSICAL_DEVICE = "http://localhost:8080/";
    private static final String BASE_URL = BASE_URL_EMULATOR;
    private static Retrofit retrofit;

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(context))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL + "api/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static String getWebSocketEndpoint() {
        try {
            String base = BASE_URL.trim();
            base = base.replaceFirst("^https://", "wss://")
                    .replaceFirst("^http://", "ws://")
                    .replaceFirst("/api/?$", "");
            return base + "/ws/websocket";
        } catch (Exception e) {
            return "ws://10.0.2.2:8080/ws/websocket";
        }
    }

    public static String getFileUrl(String filename) {
        if (filename == null || filename.isEmpty()) {
            return null;
        }
        return BASE_URL + "uploads/" + filename;
    }
}
