package com.team13.campusmitra.holder.room;

public class FacultyOffice extends Room{
    private int facultyId;

    public FacultyOffice(int roomId, String roomNumber, String roomBuilding, String roomType, String imageURL, int facultyId) {
        super(roomId, roomNumber, roomBuilding, roomType, imageURL);
        this.facultyId = facultyId;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }
}
