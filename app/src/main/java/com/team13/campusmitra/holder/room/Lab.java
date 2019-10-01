package com.team13.campusmitra.holder.room;

public class Lab extends Room{
    private String labName;
    private String labPage;
    private String labImage;
    private int systemCount;
    private int labId;

    public Lab(int roomId, String roomNumber, String roomBuilding, String roomType, String imageURL, String labName, String labPage, String labImage, int systemCount, int labId) {
        super(roomId, roomNumber, roomBuilding, roomType, imageURL);
        this.labName = labName;
        this.labPage = labPage;
        this.labImage = labImage;
        this.systemCount = systemCount;
        this.labId = labId;
    }

    public int getLabId() {
        return labId;
    }

    public void setLabId(int labId) {
        this.labId = labId;
    }

    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }

    public String getLabPage() {
        return labPage;
    }

    public void setLabPage(String labPage) {
        this.labPage = labPage;
    }

    public String getLabImage() {
        return labImage;
    }

    public void setLabImage(String labImage) {
        this.labImage = labImage;
    }

    public int getSystemCount() {
        return systemCount;
    }

    public void setSystemCount(int systemCount) {
        this.systemCount = systemCount;
    }
}
