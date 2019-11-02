package com.team13.campusmitra.adaptors;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.team13.campusmitra.BookingDialogue;
import com.team13.campusmitra.R;
import com.team13.campusmitra.dataholder.Room;
import com.team13.campusmitra.popupmanager.PopupManager;

import java.util.ArrayList;

public class VacantRoomRecyclerViewAdaptor extends RecyclerView.Adapter<VacantRoomRecyclerViewAdaptor.ViewHolder> {

    private Room[] room;
    private ArrayList<Room> roomArrayList;
    private FragmentActivity activity;
    private Context context;
    private DialogFragment fragment;

    public VacantRoomRecyclerViewAdaptor(ArrayList<Room> roomArrayList, FragmentActivity activity, Context context) {
        this.roomArrayList = roomArrayList;
        this.activity = activity;
        this.context = context;
    }

    public VacantRoomRecyclerViewAdaptor(Room[] room) {
        this.room = room;
    }

    public VacantRoomRecyclerViewAdaptor(ArrayList<Room> roomArrayList) {
        this.roomArrayList = roomArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View listItem = inflater.inflate(R.layout.recyclerview_vacant_room, parent, false);
        VacantRoomRecyclerViewAdaptor.ViewHolder holder = new VacantRoomRecyclerViewAdaptor.ViewHolder(listItem);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Room roomObject;
        if(roomArrayList!=null){
            roomObject = roomArrayList.get(position);
        }
        else{
            roomObject = room[position];
        }
        holder.roomNumber.setText(roomObject.getRoomNumber());
        holder.roomBuilding.setText(roomObject.getRoomBuilding());
        holder.bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String uid = auth.getUid();
                String roomid = roomObject.getRoomID();
                //PopupManager manager = new PopupManager(context,activity);




                BookingDialogue bookingDialogue = new BookingDialogue(BookingDialogue.ROOMBOOKING);
                bookingDialogue.setBookingDetails(uid,roomid);
                bookingDialogue.show(activity.getSupportFragmentManager(),"What the hell");
            }
        });
        Glide.with(activity)
                .asBitmap()
                .load(roomObject.getRoomImageURL())
                .placeholder(R.drawable.ic_loading)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        if(roomArrayList!=null)
        return roomArrayList.size();
        else{
            return room.length;
        }
    }

    public  static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView roomNumber;
        TextView roomBuilding;
        Button bookButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recyclerView_vacant_room_image);
            roomNumber = itemView.findViewById(R.id.recyclerView_vacant_room_number);
            roomBuilding = itemView.findViewById(R.id.recyclerView_vacant_room_building);
            bookButton = itemView.findViewById(R.id.recyclerView_vacant_room_book_btn);
        }
    }

    public void showBookingDialog(){
        TextView dateTextView = activity.findViewById(R.id.appointment_date_picker);

    }
    public void filter(ArrayList<Room> rm){
        this.roomArrayList = rm;
        this.notifyDataSetChanged();
    }
}

