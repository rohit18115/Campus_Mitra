package com.team13.campusmitra.dataholder;

import java.io.Serializable;

public class Booking implements Serializable {
    private String bookingID;
    private String roomID;
    private String date;
    private String startTime;
    private String endTime;
    private String description;
    private String userID;
    public Booking(){
        bookingID=null;
        roomID=null;
        date=null;
        startTime=null;
        endTime=null;
        description=null;
        userID=null;
    }
    public Booking(String bookingID, String roomID, String date, String startTime, String endTime, String description, String userID) {
        this.bookingID = bookingID;
        this.roomID = roomID;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.userID = userID;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
