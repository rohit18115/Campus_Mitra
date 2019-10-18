package com.team13.campusmitra.dataholder;

import java.util.ArrayList;

public class TimeTable {
    private ArrayList<TimeTableElement> timeTable;

    public TimeTable(ArrayList<TimeTableElement> timeTable) {
        this.timeTable = timeTable;
    }
    public TimeTable(){
        this.timeTable = new ArrayList<>();

    }

    private void addToTimeTable(TimeTableElement tt){
        timeTable.add(tt);
    }
    public int addTimeTableElement(TimeTableElement tt){
        for(TimeTableElement ele: timeTable){
            if(ele.getStartTime()==tt.getStartTime()){
                if(ele.getDay()==tt.getDay()){
                    if(ele.getRoomID()==tt.getRoomID()){
                        return -1; //Room already taken by some other entry
                    }
                }

            }
        }
        addToTimeTable(tt);
        return 0;
    }
    public void removeFromTimeTable(TimeTableElement element){

        timeTable.remove(element);
    }
    public ArrayList<TimeTableElement> getTimeTable() {
        return timeTable;
    }

    public void setTimeTable(ArrayList<TimeTableElement> timeTable) {
        this.timeTable = timeTable;
    }
}
