package com.example.login.api;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class StompClient {

    private final String url;
    private final OkHttpClient client = new OkHttpClient();
    private WebSocket ws;
    private final AtomicInteger subId = new AtomicInteger(1);
    private final Map<String, StompMessageListener> listeners = new HashMap<>();

    public interface StompMessageListener {
        void onMessage(String body);
    }

    public StompClient(String url) {
        this.url = url;
    }

    public void connect() {
        Request req = new Request.Builder().url(url).build();
        ws = client.newWebSocket(req, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, okhttp3.Response response) {
                webSocket.send("CONNECT\naccept-version:1.2\n\n\u0000");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                if (text.startsWith("MESSAGE")) {
                    int idx = text.indexOf("\n\n");
                    if (idx != -1) {
                        String headersPart = text.substring(0, idx);
                        String bodyPart = text.substring(idx + 2, text.indexOf('\u0000'));
                        String dest = null;
                        for (String line : headersPart.split("\n")) {
                            if (line.startsWith("destination:"))
                                dest = line.substring("destination:".length());
                        }
                        if (dest != null && listeners.containsKey(dest))
                            listeners.get(dest).onMessage(bodyPart);
                    }
                }
            }
        });
    }

    public void disconnect() {
        if (ws != null) {
            ws.send("DISCONNECT\n\n\u0000");
            ws.close(1000, null);
        }
    }

    public void subscribe(String destination, StompMessageListener listener) {
        if (ws == null) return;
        String id = "sub-" + subId.getAndIncrement();
        listeners.put(destination, listener);
        String frame = "SUBSCRIBE\nid:" + id + "\ndestination:" + destination + "\n\n\u0000";
        ws.send(frame);
    }

    public void send(String destination, String bodyJson) {
        if (ws == null) return;
        String frame = "SEND\ndestination:" + destination + "\ncontent-type:application/json\n\n" + bodyJson + "\u0000";
        ws.send(frame);
    }
}
