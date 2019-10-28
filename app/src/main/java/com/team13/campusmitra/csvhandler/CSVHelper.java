package com.team13.campusmitra.csvhandler;

import com.opencsv.CSVReader;
import com.team13.campusmitra.dataholder.Course;
import com.team13.campusmitra.dataholder.Room;
import com.team13.campusmitra.dataholder.TimeTableElement;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVHelper {
    private FileReader reader;
    private CSVReader csvReader ;
    private ArrayList<TimeTableElement> courseNotSet;
    private ArrayList<TimeTableElement> roomNotSet;

    public CSVHelper(String file){
        try {
            reader = new FileReader(file);
            csvReader  = new CSVReader(reader);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    public List<String[]> readAll(){
        try {
            return csvReader.readAll();

        }
        catch (Exception e){
            return null;
        }
    }

    private String getCourseID(String courseCode,ArrayList<Course> courses){
        for(Course course:courses){
            if(course.getCourseCode().toLowerCase().equals(courseCode)){
                return course.getCourseID();
            }
        }
        return null;

    }
    private String getRoomID(String roomNumber,ArrayList<Room> rooms){
        for(Room room:rooms){
            if(room.getRoomNumber().toLowerCase().contains(roomNumber)){
                return room.getRoomID();
           }
        }
        return null;
    }
    public List<TimeTableElement> readTimeTable(ArrayList<Course> courses, ArrayList<Room> rooms){
        List<TimeTableElement> timeTable = new ArrayList<>();
        courseNotSet = new ArrayList<>();
        String[] record = null;
        try {


        while ((record = csvReader.readNext()) != null) {
                   boolean writeFlagc= false;
                   boolean writeFlagr = false;
                   TimeTableElement tt = new TimeTableElement();
                   String cid = getCourseID(record[1],courses);
                   if(cid==null){
                       writeFlagc=true;
                   }
                   tt.setCourseID(cid);
                   tt.setStartTime(record[2]);
                   tt.setEndTime(record[3]);
                   tt.setDay(record[4]);
                   String rid= getRoomID(record[5],rooms);
                   if (rid == null) {
                        writeFlagr=true;
                   }
                   tt.setRoomID(rid);
                   tt.setTimeTableID(record[0]);
                   if (writeFlagc){
                       courseNotSet.add(tt);
                   }
                   else if(writeFlagr){
                       roomNotSet.add(tt);
                   }
                   else{
                       timeTable.add(tt);
                   }

        }
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
        return timeTable;
    }
}
