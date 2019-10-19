package com.team13.campusmitra.dataholder;

import java.io.Serializable;

public class LabRoom extends  Room implements Serializable {

    private int capacity;
    private String labName;
    private int systemCount;

    public LabRoom(int capacity, String labName, int systemCount) {
        this.capacity = capacity;
        this.labName = labName;
        this.systemCount = systemCount;
    }

    public LabRoom(String roomID, String roomNumber, String roomBuilding, int roomType, String roomImageURL, String roomDescription, int capacity, String labName, int systemCount) {
        super(roomID, roomNumber, roomBuilding, roomType, roomImageURL, roomDescription);
        this.capacity = capacity;
        this.labName = labName;
        this.systemCount = systemCount;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }

    public int getSystemCount() {
        return systemCount;
    }

    public void setSystemCount(int systemCount) {
        this.systemCount = systemCount;
    }
}
