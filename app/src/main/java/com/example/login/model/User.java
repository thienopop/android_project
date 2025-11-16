package com.example.login.model;

public class User {
    private String username;
    private String email;
    private String password; // ⚠️ đổi từ passwordHash → password để đồng bộ với form đăng nhập
    private String role;

    // 🔹 Constructor trống (Retrofit/Gson cần để parse JSON)
    public User() {
    }

    // 🔹 Constructor đầy đủ
    public User(String username, String email, String passwordHash, String role) {
        this.username = username;
        this.email = email;
        this.password = passwordHash;
        this.role=role;
    }

    // 🔹 Getter & Setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String passwordHash) {
        this.password = passwordHash;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

}
