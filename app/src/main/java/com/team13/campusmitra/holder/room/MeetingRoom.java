package com.team13.campusmitra.holder.room;

public class MeetingRoom extends Room{
    private int roomCapacity;
    private int meetingRoomId;

    public MeetingRoom(int roomId, String roomNumber, String roomBuilding, String roomType, String imageURL, int roomCapacity, int meetingRoomId) {
        super(roomId, roomNumber, roomBuilding, roomType, imageURL);
        this.roomCapacity = roomCapacity;
        this.meetingRoomId = meetingRoomId;
    }

    public int getRoomCapacity() {
        return roomCapacity;
    }

    public int getMeetingRoomId() {
        return meetingRoomId;
    }

    public void setMeetingRoomId(int meetingRoomId) {
        this.meetingRoomId = meetingRoomId;
    }

    public void setRoomCapacity(int roomCapacity) {
        this.roomCapacity = roomCapacity;
    }
}