package com.example.login.model;



public class User {
    private String username;
    private String email;
    private String passwordHash;
    private String fullName;
    private String phone;

    public User(String username, String email, String passwordHash, String fullName, String phone) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.phone = phone;
    }

    // Getter & Setter (có thể generate tự động)
}
