package com.team13.campusmitra.dataholder;

import java.util.ArrayList;

public class Faculty extends User {
    private String designation;
    private String joinedDate;
    private int avalability;
    private ArrayList<OfficeHours> officeHours;
    private ArrayList<String> coursesTaken;
    private String roomid;
    private ArrayList<String> domains;

    public Faculty(){
        super();
    }
    public Faculty(String userId, String userName, String userFirstName, String userLastName, String gender, String dob, int userType, String userEmail, String userPersonalMail, int profileCompleteCount, long lastLoginTimeStamp, int activeStatus, String imageUrl, String designation, String joinedDate) {
        super(userId, userName, userFirstName, userLastName, gender, dob, userType, userEmail, userPersonalMail, profileCompleteCount, lastLoginTimeStamp, activeStatus, imageUrl);
        this.designation = designation;
        officeHours =  new ArrayList<>();
        coursesTaken = new ArrayList<>();
        domains = new ArrayList<>();
        this.joinedDate = joinedDate;
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
