package com.team13.campusmitra.dataholder;

import java.io.Serializable;
import java.util.ArrayList;

public class Faculty implements Serializable {
    private String userID;
    private String designation;
    private int availability;
    private ArrayList<OfficeHours> officeHours;
    private ArrayList<String> coursesTaken;
    private String roomid;
    private String department;
    private ArrayList<String> domains;

    public Faculty() {
        officeHours = new ArrayList<>();
        coursesTaken = new ArrayList<>();
    }

    public Faculty(String userID, String designation, int availability, ArrayList<OfficeHours> officeHours, ArrayList<String> coursesTaken, String roomid, String department, ArrayList<String> domains) {
        this.userID = userID;
        this.designation = designation;
        this.availability = availability;
        this.officeHours = officeHours;
        this.coursesTaken = coursesTaken;
        this.roomid = roomid;
        this.department = department;
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

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
