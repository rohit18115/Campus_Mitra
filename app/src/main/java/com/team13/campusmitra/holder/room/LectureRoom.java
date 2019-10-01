package com.team13.campusmitra.holder.room;


public class LectureRoom extends Room{
    private int roomCapicity;
    private int lectureRoomId;

    public LectureRoom(int roomId, String roomNumber, String roomBuilding, String roomType, String imageURL, int roomCapicity, int lectureRoomId) {
        super(roomId, roomNumber, roomBuilding, roomType, imageURL);
        this.roomCapicity = roomCapicity;
        this.lectureRoomId = lectureRoomId;
    }

    public int getLectureRoomId() {
        return lectureRoomId;
    }

    public void setLectureRoomId(int lectureRoomId) {
        this.lectureRoomId = lectureRoomId;
    }

    public int getRoomCapicity() {
        return roomCapicity;
    }

    public void setRoomCapicity(int roomCapicity) {
        this.roomCapicity = roomCapicity;
    }
}
