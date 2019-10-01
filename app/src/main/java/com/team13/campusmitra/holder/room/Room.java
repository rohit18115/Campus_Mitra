package com.team13.campusmitra.holder.room;

public class Room{
    private String roomNumber;
    private String roomBuilding;
    private String roomType;
    private String imageURL;
    int roomId;

    public Room(int roomId,String roomNumber, String roomBuilding, String roomType, String imageURL) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.roomBuilding = roomBuilding;
        this.roomType = roomType;
        this.imageURL = imageURL;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
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

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}