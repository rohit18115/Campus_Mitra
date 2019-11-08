package com.team13.campusmitra.adaptors;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.team13.campusmitra.R;
import com.team13.campusmitra.dataholder.Booking;
import com.team13.campusmitra.dataholder.Room;
import com.team13.campusmitra.firebaseassistant.FirebaseAppointmentHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseBookingHelper;

import java.util.ArrayList;

public class BookingListAdaptor extends RecyclerView.Adapter<BookingListAdaptor.MYViewHolder>{
    public ArrayList<Room> rooms;
    public ArrayList<Booking> bookings;
    private Context context;

    public BookingListAdaptor(ArrayList<Room> rooms, ArrayList<Booking> bookings,Context context) {
        this.rooms = rooms;
        this.bookings = bookings;
        this.context = context;
    }

    @NonNull
    @Override
    public MYViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.booking_recycler_view,parent,false);
        MYViewHolder holder = new MYViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MYViewHolder holder, int position) {
        final Booking booking = bookings.get(position);
        holder.roomNumber.setText(getRoomNumber(booking.getRoomID()));
        holder.toTime.setText(booking.getEndTime());;
        holder.fromTime.setText(booking.getStartTime());
        holder.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             remove(booking);
            }
        });
    }
    private void remove(Booking booking){
        FirebaseBookingHelper helper = new FirebaseBookingHelper();
        DatabaseReference reference = helper.getReference().child(booking.getBookingID());
        reference.removeValue();

    }
    private String getRoomNumber(String roomid){
        for(Room r:rooms){
            if(roomid.toLowerCase().equals(r.getRoomID().toLowerCase())){
                return r.getRoomNumber();
            }
        }
        return "";
    }
    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public static class MYViewHolder extends RecyclerView.ViewHolder{
    TextView roomNumber;
    TextView fromTime;
    TextView toTime;
    Button cancelButton;
    public MYViewHolder(@NonNull View itemView) {
        super(itemView);
        roomNumber = itemView.findViewById(R.id.booking_list_room_number);
        fromTime=itemView.findViewById(R.id.booking_list_starttime);
        toTime = itemView.findViewById(R.id.booking_list_endtime);
        cancelButton = itemView.findViewById(R.id.booking_list_cancel);

    }
}
}
