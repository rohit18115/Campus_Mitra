package com.team13.campusmitra.dataholder;

public class TimeTableElement {
    private String timeTableID;
    private String courseID;
    private String startTime;
    private String endTime;
    private String roomID;
    private String day;

    public TimeTableElement(String timeTableID, String courseID, String startTime, String endTime, String roomID, String day) {
        this.timeTableID = timeTableID;
        this.courseID = courseID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.roomID = roomID;
        this.day = day;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTimeTableID() {
        return timeTableID;
    }

    public void setTimeTableID(String timeTableID) {
        this.timeTableID = timeTableID;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
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

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }
}
