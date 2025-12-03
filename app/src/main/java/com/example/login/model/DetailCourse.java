package com.example.login.model;

public class DetailCourse  {
//    text_complete_sessions, text_notes ,text_status, text_end_date, text_start_date,text_total_price, text_total_sessions, text_subject, text_full_name;

    private int id;

    // 2. Tên đầy đủ của học viên hoặc giáo viên
    private String fullName;
    private int userId;
//    text_complete_sessions
    // 3. Môn học
    private String subject;

    // 4. Tổng số buổi học
    private int totalSessions;
    private int completedSessions;

    // 5. Tổng chi phí (Sử dụng Double cho tiền tệ)
    private double totalPrice;

    // 6. Thời gian của buổi học (Ví dụ: "Thứ 3 & Thứ 5")
    private String timeOfTheLesson;


    // 10. Ngày bắt đầu khóa học
    private String startDate;

    // 11. Ngày kết thúc khóa học
    private String endDate;

    // 12. Trạng thái
    private String status;

    // 13. Ghi chú (Cho phép giá trị null)
    private String notes;

    /**
     * Constructor mặc định (cần thiết cho một số thư viện như Firebase, Gson).
     */
    public DetailCourse () {
        // Constructor rỗng
    }


    public DetailCourse (int id, String fullName, int userId, String subject, int totalSessions, int  completedSessions, double totalPrice, String timeOfTheLesson, String startTime, String endTime, String createdAt, String startDate, String endDate, String status, String notes) {
        this.id = id;
        this.fullName = fullName;
        this.userId=userId;
        this.subject = subject;
        this.totalSessions = totalSessions;
        this.totalPrice = totalPrice;
        this.timeOfTheLesson = timeOfTheLesson;
this.completedSessions= completedSessions;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.notes = notes;
    }

    // --- GETTERS & SETTERS (Truy cập và sửa đổi thuộc tính) ---

    public int getId() {
        return id;
    }

    public int getCompletedSessions()
    {
    return completedSessions;
        }
    public void setCompletedSessions( int completedSessions)
    {
        this.completedSessions= completedSessions;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }



    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getUserId()
    {
        return userId;
    }
    public void setUserId(int userId)
    {
        this.userId=userId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getTotalSessions() {
        return totalSessions;
    }

    public void setTotalSessions(int totalSessions) {
        this.totalSessions = totalSessions;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTimeOfTheLesson() {
        return timeOfTheLesson;
    }
    public void setTimeOfTheLesson(String timeOfTheLesson) {
        this.timeOfTheLesson = timeOfTheLesson;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    public void setCompleteSession(int count)
    {
        this.completedSessions=count;
    }
}