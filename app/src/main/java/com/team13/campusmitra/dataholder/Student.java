package com.team13.campusmitra.dataholder;

import java.util.ArrayList;

public class Student extends User {

    private String rollNumber;
    private String enrollCourse;
    private String areaOfInterest;
    private String resumeURL;
    private ArrayList<String> courses;

    public Student()
    {
        super();
    }
    public Student(String userId, String userName, String userFirstName, String userLastName, String gender, String dob, int userType, String userEmail, String userPersonalMail, int profileCompleteCount, long lastLoginTimeStamp, int activeStatus, String imageUrl, String rollNumber, String enrollCourse) {
        super(userId, userName, userFirstName, userLastName, gender, dob, userType, userEmail, userPersonalMail, profileCompleteCount, lastLoginTimeStamp, activeStatus, imageUrl);
        this.rollNumber = rollNumber;
        this.enrollCourse = enrollCourse;
        courses = new ArrayList<>();
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getEnrollCourse() {
        return enrollCourse;
    }

    public void setEnrollCourse(String enrollCourse) {
        this.enrollCourse = enrollCourse;
    }

    public String getAreaOfInterest() {
        return areaOfInterest;
    }

    public void setAreaOfInterest(String areaOfInterest) {
        this.areaOfInterest = areaOfInterest;
    }

    public String getResumeURL() {
        return resumeURL;
    }

    public void setResumeURL(String resumeURL) {
        this.resumeURL = resumeURL;
    }

    public ArrayList<String> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<String> courses) {
        this.courses = courses;
    }
    public void addCourse(String course){
        courses.add(course);
    }
    public void removeCourse(String course){
        courses.remove(course);
    }
}
