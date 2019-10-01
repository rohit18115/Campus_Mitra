package com.team13.campusmitra.holder.room;

public class ResearchLab extends Room{
    private int researchLabId;
    private String researchLabName;
    private String labImageUrl;
    private String labVideoUrl;
    private String labWebUrl;
    private int faculty;

    public ResearchLab(int roomId, String roomNumber, String roomBuilding, String roomType, String imageURL, int researchLabId, String researchLabName, String labImageUrl, String labVideoUrl, String labWebUrl, int faculty) {
        super(roomId, roomNumber, roomBuilding, roomType, imageURL);
        this.researchLabId = researchLabId;
        this.researchLabName = researchLabName;
        this.labImageUrl = labImageUrl;
        this.labVideoUrl = labVideoUrl;
        this.labWebUrl = labWebUrl;
        this.faculty = faculty;
    }

    public int getResearchLabId() {
        return researchLabId;
    }

    public void setResearchLabId(int researchLabId) {
        this.researchLabId = researchLabId;
    }

    public String getResearchLabName() {
        return researchLabName;
    }

    public void setResearchLabName(String researchLabName) {
        this.researchLabName = researchLabName;
    }

    public String getLabImageUrl() {
        return labImageUrl;
    }

    public void setLabImageUrl(String labImageUrl) {
        this.labImageUrl = labImageUrl;
    }

    public String getLabVideoUrl() {
        return labVideoUrl;
    }

    public void setLabVideoUrl(String labVideoUrl) {
        this.labVideoUrl = labVideoUrl;
    }

    public String getLabWebUrl() {
        return labWebUrl;
    }

    public void setLabWebUrl(String labWebUrl) {
        this.labWebUrl = labWebUrl;
    }

    public int getFaculty() {
        return faculty;
    }

    public void setFaculty(int faculty) {
        this.faculty = faculty;
    }
}
