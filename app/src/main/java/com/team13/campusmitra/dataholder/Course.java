package com.team13.campusmitra.dataholder;

import java.io.Serializable;

public class Course implements Serializable {
    private String courseID;
    private String courseCode;
    private String courseName;
    private String coursePrequisite;
    private String offering;
    private String facultyEmail;

    public String getFacultyEmail() {
        return facultyEmail;
    }

    public void setFacultyEmail(String facultyEmail) {
        this.facultyEmail = facultyEmail;
    }

    public Course(String courseID, String courseCode, String courseName, String coursePrequisite, String offering, String facultyEmail) {
        this.courseID = courseID;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.coursePrequisite = coursePrequisite;
        this.offering = offering;
        this.facultyEmail = facultyEmail;
    }

    public Course(String courseID, String courseCode, String courseName) {
        this.courseID = courseID;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.coursePrequisite=null;
        this.offering = null;
    }

    public Course(String courseCode, String courseName, String coursePrequisite, String offering) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.coursePrequisite = coursePrequisite;
        this.offering = offering;
    }

    public Course(String courseID, String courseCode, String courseName, String coursePrequisite, String offering) {
        this.courseID = courseID;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.coursePrequisite = coursePrequisite;
        this.offering = offering;
    }

    public Course() {

    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCoursePrequisite() {
        return coursePrequisite;
    }

    public void setCoursePrequisite(String coursePrequisite) {
        this.coursePrequisite = coursePrequisite;
    }

    public String getOffering() {
        return offering;
    }

    public void setOffering(String offering) {
        this.offering = offering;
    }

}
