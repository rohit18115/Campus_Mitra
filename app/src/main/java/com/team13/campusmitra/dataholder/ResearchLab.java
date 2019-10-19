package com.team13.campusmitra.dataholder;

import java.io.Serializable;
import java.util.ArrayList;

public class ResearchLab implements Serializable {
    private  String roomID;
    private String researchLabName;
    private String imageURL;
    private String videoURL;
    private ArrayList<String> mentors;
    private String webPageURL;
    private ArrayList<String> projects;

    public ResearchLab(String roomID){
        this.roomID = roomID;
        mentors = new ArrayList<>();
        projects= new ArrayList<>();
    }

    public ResearchLab(String roomID, String researchLabName, String imageURL, String videoURL, ArrayList<String> mentors, String webPageURL, ArrayList<String> projects) {
        this.roomID = roomID;
        this.researchLabName = researchLabName;
        this.imageURL = imageURL;
        this.videoURL = videoURL;
        this.mentors = mentors;
        this.webPageURL = webPageURL;
        this.projects = projects;
    }

    public void addProject(String project){
        projects.add(project);
    }
    public void removeProject(String project){
        projects.remove(project);
    }
    public void addMentor(String mentorID){
        mentors.add(mentorID);
    }
    public void removeMentor(String mentorID){
        mentors.remove(mentorID);
    }
       public String getResearchLabName() {
        return researchLabName;
    }

    public void setResearchLabName(String researchLabName) {
        this.researchLabName = researchLabName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public ArrayList<String> getMentors() {
        return mentors;
    }

    public void setMentors(ArrayList<String> mentors) {
        this.mentors = mentors;
    }

    public String getWebPageURL() {
        return webPageURL;
    }

    public void setWebPageURL(String webPageURL) {
        this.webPageURL = webPageURL;
    }

    public ArrayList<String> getProjects() {
        return projects;
    }

    public void setProjects(ArrayList<String> projects) {
        this.projects = projects;
    }

}
