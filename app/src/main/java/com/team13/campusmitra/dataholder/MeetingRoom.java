package com.team13.campusmitra.dataholder;

import java.io.Serializable;

public class MeetingRoom extends Room implements Serializable {
    private int capacity;

    public MeetingRoom() {
        super();
    }

    public MeetingRoom(String roomID, String roomNumber, String roomBuilding, int roomType, String roomImageURL, String roomDescription, int capacity) {
        super(roomID, roomNumber, roomBuilding, roomType, roomImageURL, roomDescription);
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
