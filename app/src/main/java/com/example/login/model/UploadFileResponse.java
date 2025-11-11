package com.example.login.model;

public class UploadFileResponse {

    private String message;
    private String fileName;
    private String fileUrl;

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
}