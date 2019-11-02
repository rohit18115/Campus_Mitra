package com.team13.campusmitra.dataholder;

import java.io.Serializable;

public class Appointment implements Serializable {
    private String appointmentID;
    private String userID1; // Who take appointment
    private String userID2; // With whom appointment is taken
    private String date;
    private String time;
    private String description;
    private String appointmentStatus;

    public String getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getUserID1() {
        return userID1;
    }

    public void setUserID1(String userID1) {
        this.userID1 = userID1;
    }

    public String getUserID2() {
        return userID2;
    }

    public void setUserID2(String userID2) {
        this.userID2 = userID2;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String startTime) {
        this.time = startTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public Appointment() {
        this.appointmentID = null;
        this.userID1 = null;
        this.userID2 =null;
        this.date = null;
        time =null;
        this.description = null;
        this.appointmentStatus = null;
    }

    public Appointment(String appointmentID, String userID1, String userID2, String date, String time, String description, String appointmentStatus) {
        this.appointmentID = appointmentID;
        this.userID1 = userID1;
        this.userID2 = userID2;
        this.date = date;
        this.time = time;
        this.description = description;
        this.appointmentStatus = appointmentStatus;
    }
}
