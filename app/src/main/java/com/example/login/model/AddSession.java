package com.example.login.model;

import java.time.LocalDateTime;

public class AddSession {

    private Integer courseId;


    private String sessionDate;


    private Integer duration;

    private String notes;



    public AddSession( int duration,String notes, String sessionDate, int courseId) {

        this.duration = duration;
        this.courseId=courseId;
        this.notes = notes;
        this.sessionDate = sessionDate;
    }

    public int getDuration() { return duration; }

    public int getCourseId() { return courseId; }
    public String getNotes() { return notes; }
    public String getSessionDate() { return sessionDate; }

}
