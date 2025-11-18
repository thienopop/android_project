package com.example.login.model;

public class RegisterLoginResponse{
    private String message;
    private String token;
    private String role;

    public String getMessage() { return message; }
    public String getToken() { return token; }
    public String getRole(){
        return role;
    }
}
