package com.example.login.model;

public class Notification {
        private int id;
        private int userId;
        private String title;
        private String message;
        private String type;
        private String redirectUrl;
        private Boolean isRead;
        private String readAt;
    private String createdAt;
    private String updatedAt;
    public int getId() {
    return id;}

    public int getUserId() {
    return userId;}
    public void setUserId(int userId) {
        this.userId=userId;}

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
      this.title=title;
    }


    public String getMessage()
   {
       return message;
   }
    public void setMessage(String message)
    {
 this.message=message;
    }

    public  Boolean getIsRead()
    {
        return isRead;
    }
    public  void setIsRead(Boolean isRead)
    {
        this.isRead=isRead;
    }

    public String getCreatedAt()
    {
        return createdAt;
    }




}
