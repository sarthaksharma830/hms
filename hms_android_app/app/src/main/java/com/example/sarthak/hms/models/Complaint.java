package com.example.sarthak.hms.models;

import java.util.Date;

public class Complaint {
    private int id;
    private String title;
    private Student student;
    private ComplaintCategory complaintCategory;
    private Date dateTime;
    private String description;
    private String feedback;
    private ComplaintStatus complaintStatus;
    private boolean starred;
    private Date appointmentDatePreference;
    private Date appointmentFromTimePreference;
    private Date appointmentToTimePreference;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public ComplaintCategory getComplaintCategory() {
        return complaintCategory;
    }

    public void setComplaintCategory(ComplaintCategory complaintCategory) {
        this.complaintCategory = complaintCategory;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public ComplaintStatus getComplaintStatus() {
        return complaintStatus;
    }

    public void setComplaintStatus(ComplaintStatus complaintStatus) {
        this.complaintStatus = complaintStatus;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public Date getAppointmentDatePreference() {
        return appointmentDatePreference;
    }

    public void setAppointmentDatePreference(Date appointmentDatePreference) {
        this.appointmentDatePreference = appointmentDatePreference;
    }

    public Date getAppointmentFromTimePreference() {
        return appointmentFromTimePreference;
    }

    public void setAppointmentFromTimePreference(Date appointmentFromTimePreference) {
        this.appointmentFromTimePreference = appointmentFromTimePreference;
    }

    public Date getAppointmentToTimePreference() {
        return appointmentToTimePreference;
    }

    public void setAppointmentToTimePreference(Date appointmentToTimePreference) {
        this.appointmentToTimePreference = appointmentToTimePreference;
    }
}
