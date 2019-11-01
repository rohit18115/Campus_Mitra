package com.team13.campusmitra.managers;

import com.team13.campusmitra.dataholder.Booking;
import com.team13.campusmitra.dataholder.Room;
import com.team13.campusmitra.dataholder.TimeTable;
import com.team13.campusmitra.dataholder.TimeTableElement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

public class VacantRoomManager {
    private ArrayList<Room> rooms;
    private ArrayList<TimeTableElement> elements;
    private ArrayList<Booking> bookings;

    private int startTime;
    private int endTime;
    private String day;
    private String date;


    public String getDate() {
        return date;
    }


    public void setDate(String date) {
        this.date = date;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public VacantRoomManager(ArrayList<Room> rooms, ArrayList<TimeTableElement> elements, ArrayList<Booking> bookings) {
        this.rooms = rooms;
        this.elements = elements;
        this.bookings = bookings;
    }

    public VacantRoomManager() {
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    public ArrayList<TimeTableElement> getElements() {
        return elements;
    }

    public void setElements(ArrayList<TimeTableElement> elements) {
        this.elements = elements;
    }

    public ArrayList<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(ArrayList<Booking> bookings) {
        this.bookings = bookings;
    }

    ArrayList<Room> getVacantRoomsWithCapacity(int capacity){
        ArrayList<Room> vacantRooms = new ArrayList<>();

        if(rooms==null){
            return null;
        }
        for(Room r:rooms){
            if(r.getRoomType()==0) {
                if (r.getCapacity()>=capacity) {
                    vacantRooms.add(r);
                }
            }
        }
        ArrayList<TimeTableElement> dayElement = new ArrayList<>();

        for(TimeTableElement element:elements){
            if(element.getDay().toLowerCase()==getDayByDate(this.date).toLowerCase()){
                dayElement.add(element);
            }

        }
        for (TimeTableElement element:dayElement){
            if(!checkOverlap(this.startTime,this.endTime,Integer.parseInt(element.getStartTime()),Integer.parseInt(element.getEndTime()))){
                String roomid=element.getRoomID();
                for(int i =0;i<vacantRooms.size();i++){
                    if(vacantRooms.get(i).getRoomID().toLowerCase().equals(roomid)){
                        vacantRooms.remove(i);
                        break;
                    }
                }
            }
        }
        ArrayList<Booking> book = new ArrayList<>();

        for(Booking booking:bookings){
            if (booking.getDate().toLowerCase().equals(this.date)){
                book.add(booking);
            }
        }
        for(Booking booking:book){

            if(!checkOverlap(this.startTime,this.endTime,Integer.parseInt(booking.getStartTime()),Integer.parseInt(booking.getEndTime()))){
                    String roomid = booking.getRoomID();
                for(int i =0;i<vacantRooms.size();i++){
                    if(vacantRooms.get(i).getRoomID().toLowerCase().equals(roomid)){
                        vacantRooms.remove(i);
                        break;
                    }
                }
            }
        }

        return vacantRooms;
    }
    boolean checkOverlap(int startTime,int endTime,int classStartTime,int classEndTime){
        if(startTime>classEndTime && classStartTime>endTime){
            return false;
        }
        else{
            return true;
        }

    }
    String getDayByDate(String date){
        StringTokenizer tokenizer = new StringTokenizer("-");
        String dat[] = new String[3];
        //String day,Month,year;
        int i=0;
        while(tokenizer.hasMoreElements()&&i<3){
            dat[i]= tokenizer.nextToken();
        }
        Date date1 = new Date(Integer.parseInt(dat[2]),Integer.parseInt(dat[1]),Integer.parseInt(dat[0]));
        Calendar cal = Calendar.getInstance();
        int d = cal.get(Calendar.DAY_OF_MONTH);
        switch (d){
            case 1: return "Sunday";
            case 2: return "Monday";
            case 3: return "Tuesday";
            case 4: return "Wednesday";
            case 5: return "Thursday";
            case 6: return "Friday";
            case 7: return "Saturday";
        }
        return "";

    }
    ArrayList<Room> getVacantRooms(){
      ArrayList<Room> vacantRooms = new ArrayList<>();

      return vacantRooms;
    }


}
