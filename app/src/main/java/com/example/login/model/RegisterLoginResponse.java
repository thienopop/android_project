package com.example.login.model;

public class RegisterLoginResponse{
    private String message;
    private String token;
    private Integer currentUserId;

    public String getMessage() { return message; }
    public String getToken() { return token; }
    public Integer getCurrentUserId() { return currentUserId; }
}
