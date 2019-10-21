package com.team13.campusmitra.dataholder;

import java.io.Serializable;
import java.util.ArrayList;

public class Faculty implements Serializable {
    private String userID;
    private String designation;
    private String joinedDate;
    private int avalability;
    private ArrayList<OfficeHours> officeHours;
    private ArrayList<String> coursesTaken;
    private String roomid;
    private ArrayList<String> domains;

    public Faculty() {
        officeHours = new ArrayList<>();
        coursesTaken = new ArrayList<>();
    }

    public Faculty(String userID, String designation, String joinedDate, int avalability, ArrayList<OfficeHours> officeHours, ArrayList<String> coursesTaken, String roomid, ArrayList<String> domains) {
        this.userID = userID;
        this.designation = designation;
        this.joinedDate = joinedDate;
        this.avalability = avalability;
        this.officeHours = officeHours;
        this.coursesTaken = coursesTaken;
        this.roomid = roomid;
        this.domains = domains;
    }

    public void addDomains(String domain){
        domains.add(domain);
    }
    public void removeDomain(String domain){
        domains.remove(domain);
    }
    public void addCourse(String course){
        coursesTaken.add(course);
    }
    public void removeCourse(String course){
        coursesTaken.remove(course);
    }
    public void addOfficeHour(OfficeHours officeHour){
        officeHours.add(officeHour);
    }
    public void removeOfficeHour(OfficeHours officeHour){
        officeHours.remove(officeHour);
    }
    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(String joinedDate) {
        this.joinedDate = joinedDate;
    }

    public int getAvalability() {
        return avalability;
    }

    public void setAvalability(int avalability) {
        this.avalability = avalability;
    }

    public ArrayList<OfficeHours> getOfficeHours() {
        return officeHours;
    }

    public void setOfficeHours(ArrayList<OfficeHours> officeHours) {
        this.officeHours = officeHours;
    }

    public ArrayList<String> getCoursesTaken() {
        return coursesTaken;
    }

    public void setCoursesTaken(ArrayList<String> coursesTaken) {
        this.coursesTaken = coursesTaken;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public ArrayList<String> getDomains() {
        return domains;
    }

    public void setDomains(ArrayList<String> domains) {
        this.domains = domains;
    }
}
