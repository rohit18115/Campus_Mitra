package com.team13.campusmitra.managers;

import android.content.Context;

import com.team13.campusmitra.dataholder.Booking;
import com.team13.campusmitra.firebaseassistant.FirebaseBookingHelper;

public class BookingManager {

    private String roomID;
    private String date;
    private String startTime;
    private String endTime;
    private String description;
    private String userID;
    private String bookingStatus;
    private Booking booking;
    private Context context;

    public BookingManager(Context context, Booking booking) {
        this.context  = context;
        this.booking = booking;
    }

    public BookingManager(String userID, String roomID, String date, String startTime, String endTime, String description, Context context, String bookingStatus) {
        this.userID = userID;
        this.roomID = roomID;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.context = context;
        this.bookingStatus = bookingStatus;
    }

    public BookingManager(Context context) {
        this.context = context;
    }

    public BookingManager(Context context, String userID, String roomID, String date, String startTime, String endTime, String description) {
        this.userID = userID;
        this.roomID = roomID;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.context = context;
    }
    public void bookiingWithData(){
        FirebaseBookingHelper helper = new FirebaseBookingHelper();

        helper.addBooking(context,booking);
    }
    public void booking(){
        FirebaseBookingHelper helper = new FirebaseBookingHelper();
        Booking booking = new Booking();
        booking.setUserID(userID);
        booking.setRoomID(roomID);
        booking.setDate(date);
        booking.setStartTime(startTime);
        booking.setEndTime(endTime);
        booking.setDescription(description);
        booking.setBookingStatus(bookingStatus);

        helper.addBooking(context,booking);
    }

}
