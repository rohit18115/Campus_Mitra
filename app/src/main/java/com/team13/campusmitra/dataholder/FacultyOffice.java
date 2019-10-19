package com.team13.campusmitra.dataholder;

import java.io.Serializable;

public class FacultyOffice extends Room implements Serializable {
    private String roomNotes;

    public FacultyOffice(String roomNotes) {
        this.roomNotes = roomNotes;
    }

    public FacultyOffice(String roomID, String roomNumber, String roomBuilding, int roomType, String roomImageURL, String roomDescription, String roomNotes) {
        super(roomID, roomNumber, roomBuilding, roomType, roomImageURL, roomDescription);
        this.roomNotes = roomNotes;
    }

    public String getRoomNotes() {
        return roomNotes;
    }

    public void setRoomNotes(String roomNotes) {
        this.roomNotes = roomNotes;
    }
}
