package com.team13.campusmitra.holder.room;

public class DiscussionRoom extends Room{
    private int roomCapacity;

    public DiscussionRoom(int roomId, String roomNumber, String roomBuilding, String roomType, String imageURL, int roomCapacity) {
        super(roomId, roomNumber, roomBuilding, roomType, imageURL);
        this.roomCapacity = roomCapacity;
    }

    public int getRoomCapacity() {
        return roomCapacity;
    }

    public void setRoomCapacity(int roomCapacity) {
        this.roomCapacity = roomCapacity;
    }
}