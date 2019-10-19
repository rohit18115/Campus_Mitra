package com.team13.campusmitra.dataholder;

import java.io.Serializable;

public class Room implements Serializable {
    private String roomID;
    private String roomNumber;
    private String roomBuilding;
    private int roomType;
    private String roomImageURL;
    private String roomDescription; // Can be used as the flags like Cleaning, Maintainance etc.
    private int capacity;
    private String roomNotes;
    private String labName;
    private int systemCount;

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getRoomNotes() {
        return roomNotes;
    }

    public void setRoomNotes(String roomNotes) {
        this.roomNotes = roomNotes;
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

    public Room(String roomID, String roomNumber, String roomBuilding, int roomType, String roomImageURL, String roomDescription, int capacity, String roomNotes, String labName, int systemCount) {
        this.roomID = roomID;
        this.roomNumber = roomNumber;
        this.roomBuilding = roomBuilding;
        this.roomType = roomType;
        this.roomImageURL = roomImageURL;
        this.roomDescription = roomDescription;
        this.capacity = capacity;
        this.roomNotes = roomNotes;
        this.labName = labName;
        this.systemCount = systemCount;
    }

    public Room() {
        this.roomID = null;
        this.roomNumber = null;
        this.roomBuilding = null;
        this.roomType = -1;
        this.roomImageURL = null;
        this.roomDescription = null;
        this.capacity = 0;
        this.roomNotes = null;
        this.labName = null;
        this.systemCount = 0;

    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomBuilding() {
        return roomBuilding;
    }

    public void setRoomBuilding(String roomBuilding) {
        this.roomBuilding = roomBuilding;
    }

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }

    public String getRoomImageURL() {
        return roomImageURL;
    }

    public void setRoomImageURL(String roomImageURL) {
        this.roomImageURL = roomImageURL;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public Room(String roomID, String roomNumber, String roomBuilding, int roomType, String roomImageURL, String roomDescription) {
        this.roomID = roomID;
        this.roomNumber = roomNumber;
        this.roomBuilding = roomBuilding;
        this.roomType = roomType;
        this.roomImageURL = roomImageURL;
        this.roomDescription = roomDescription;
    }
    @Override
    public String toString(){
        String s = "["+getRoomNumber()+" ,"+getRoomBuilding()+"]";
        return s;
    }
}
