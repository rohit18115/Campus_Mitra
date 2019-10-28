package com.team13.campusmitra.dataholder;

import java.io.Serializable;
import java.util.ArrayList;

public class Project implements Serializable {
    private String projectName;
    private String projectID;
    private String projectDescription;
    private ArrayList<String> members;
    private String projectImageURL;
    private String projectVideoURL;
    private boolean expanded;

    public Project(String projectName, String projectDescription) {
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        members = new ArrayList<>();
        projectImageURL = null;
        projectVideoURL = null;
    }
    public Project(){

    }
    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }
    public void addProjectMember(String memberName){
        members.add(memberName);
    }
    public void removeMember(String memberName){
        members.remove(memberName);
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    public String getProjectImageURL() {
        return projectImageURL;
    }

    public void setProjectImageURL(String projectImageURL) {
        this.projectImageURL = projectImageURL;
    }

    public String getProjectVideoURL() {
        return projectVideoURL;
    }

    public void setProjectVideoURL(String projectVideoURL) {
        this.projectVideoURL = projectVideoURL;
    }
    public boolean isExpanded() {
        return expanded;
    }
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
