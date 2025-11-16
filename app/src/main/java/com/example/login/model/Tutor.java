package com.example.login.model;




import java.time.LocalDate;
import java.time.LocalDateTime;


public class Tutor {
    private int id;
    private Integer user_Id;
    private String fullName;
    private String phone;
    private String address;
    private String dateOfBirth;
    private String bio;
    private Integer experienceYears;
    private Double hourlyRate;
    private Boolean verified;
    private Integer totalSessions;
    private Double averageRating;
    private String profileImage;







//    private LocalDateTime createdAt ;
//    private LocalDateTime updatedAt;

    // ===== Constructors =====

    // ===== Getters & Setters =====
//    public LocalDateTime getUpdatedAt()
//    {
//        return updatedAt;
//    }
//    public void  setUpdatedAt( LocalDateTime localdate)
//    {
//        updatedAt= localdate;
//    }
//    public LocalDateTime getCreatedAt()
//    {
//        return createdAt;
//    }
//    public void  setCreatedAt( LocalDateTime localdate)
//    {
//        createdAt= localdate;
//    }

    public int getId() {
        return id;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateOfBirth() {
        return dateOfBirth.toString();
    }


    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public Double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Integer getTotalSessions() {
        return totalSessions;
    }

    public void setTotalSessions(Integer totalSessions) {
        this.totalSessions = totalSessions;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
//
//    public LocalDateTime getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(LocalDateTime createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public LocalDateTime getUpdatedAt() {
//        return updatedAt;
//    }
//
//    public void setUpdatedAt(LocalDateTime updatedAt) {
//        this.updatedAt = updatedAt;
//    }
    public Integer getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(Integer user_Id) {
        this.user_Id = user_Id;
    }
}
