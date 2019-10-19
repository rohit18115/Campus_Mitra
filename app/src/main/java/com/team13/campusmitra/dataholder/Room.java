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


    public Room() {
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
}
