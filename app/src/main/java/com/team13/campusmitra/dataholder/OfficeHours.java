package com.team13.campusmitra.dataholder;

import java.io.Serializable;

public class OfficeHours implements Serializable {
    private String day;
    private String startTime;
    private String endTime;
    private String venue;
    private int dnd;

    public int getDnd() {
        return dnd;
    }

    public void setDnd(int dnd) {
        this.dnd = dnd;
    }

    public OfficeHours () {

    }

    public OfficeHours(String day, String startTime, String endTime, String venue) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.venue = venue;
        this.dnd = 0;
    }

    public OfficeHours(String day, String startTime, String endTime, String venue, int dnd) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.venue = venue;
        this.dnd = dnd;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
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

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public OfficeHours(String day, String startTime, String endTime) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
