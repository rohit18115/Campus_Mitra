package com.team13.campusmitra.dataholder;

import java.io.Serializable;
import java.util.ArrayList;

public class Faculty implements Serializable {
    private String userID;
    private String designation;
    private int availability;
    //private ArrayList<OfficeHours> officeHours;
    private OfficeHours officeHours;
    private ArrayList<String> coursesTaken;
    private String roomid;
    private String roomNo;
    private String department;
    private String domains;

    public Faculty() {
        //officeHours = new ArrayList<>();
        coursesTaken = new ArrayList<>();
    }

    public Faculty(String userID, String designation, int availability, OfficeHours officeHours, ArrayList<String> coursesTaken, String roomid, String department, String domains) {
        this.userID = userID;
        this.designation = designation;
        this.availability = availability;
        this.officeHours = officeHours;
        this.coursesTaken = coursesTaken;
        this.roomid = roomid;
        this.department = department;
        this.domains = domains;
    }

//    public void addDomains(String domain){
//        domains.add(domain);
//    }
//    public void removeDomain(String domain){
//        domains.remove(domain);
//    }
    public void addCourse(String course){
        coursesTaken.add(course);
    }
    public void removeCourse(String course){
        coursesTaken.remove(course);
    }
//    public void addOfficeHour(OfficeHours officeHour){
//        officeHours.add(officeHour);
//    }
//    public void removeOfficeHour(OfficeHours officeHour){
//        officeHours.remove(officeHour);
//    }
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

//    public ArrayList<OfficeHours> getOfficeHours() {
//        return officeHours;
//    }
//
//    public void setOfficeHours(ArrayList<OfficeHours> officeHours) {
//        this.officeHours = officeHours;
//    }

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

//    public ArrayList<String> getDomains() {
//        return domains;
//    }
//
//    public void setDomains(ArrayList<String> domains) {
//        this.domains = domains;
//    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public OfficeHours getOfficeHours() {
        return officeHours;
    }

    public void setOfficeHours(OfficeHours officeHours) {
        this.officeHours = officeHours;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getDomains() {
        return domains;
    }

    public void setDomains(String domains) {
        this.domains = domains;
    }
}
