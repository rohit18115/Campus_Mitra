package com.team13.campusmitra.dataholder;

import java.io.Serializable;
import java.util.ArrayList;

public class Student  implements Serializable {
    private String userID;
    private String rollNumber;
    private String enrollCourse;
    private String areaOfInterest;
    private String resumeURL;
    private String department;
    private ArrayList<String> courses;

    public Student() {

        //courses = new ArrayList<>();
    }

    public Student(String userID, String rollNumber, String enrollCourse, String areaOfInterest, String resumeURL, String department, ArrayList<String> courses) {
        this.userID = userID;
        this.rollNumber = rollNumber;
        this.enrollCourse = enrollCourse;
        this.areaOfInterest = areaOfInterest;
        this.resumeURL = resumeURL;
        this.department = department;
        this.courses = courses;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

}
