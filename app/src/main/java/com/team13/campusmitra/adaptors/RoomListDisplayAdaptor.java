package com.team13.campusmitra.adaptors;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team13.campusmitra.EditRoomsDetails;
import com.team13.campusmitra.R;
import com.team13.campusmitra.Rooms;
import com.team13.campusmitra.dataholder.Course;
import com.team13.campusmitra.dataholder.Room;
import com.team13.campusmitra.firebaseassistant.FirebaseCoursesHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseRoomHelper;

import java.util.ArrayList;

public class RoomListDisplayAdaptor extends RecyclerView.Adapter<RoomListDisplayAdaptor.RoomViewHolder> {
    private AppCompatActivity activity;

    public RoomListDisplayAdaptor(AppCompatActivity activity, ArrayList<Room> items) {
        this.activity = activity;
        this.items = items;
    }

    private ArrayList<Room> items;
    //ImageButton imageButton;

    public RoomListDisplayAdaptor(ArrayList<Room> items,ImageButton imageButton)
    {
        this.items=items;
       // this.imageButton=imageButton;
    }
    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rooms_list, parent,false);
        RoomListDisplayAdaptor.RoomViewHolder holder = new RoomListDisplayAdaptor.RoomViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {

        final Room room = items.get(position);
        holder.rooms_textView.setText(room.getRoomNumber());

        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                return false;
            }
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(room);
            }
        });
//        Glide.with(mContext)
//                .asBitmap()
//                .load(user.getImageUrl())
//                .placeholder(R.drawable.ic_loading)
//                .into(holder.image);

    }

    @Override
    public int getItemCount() {

        return items.size();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder{
        ImageView rooms_imagebutton;
        TextView rooms_textView;
        LinearLayout linearLayout;
        public RoomViewHolder(View view)
        {
            super(view);
            rooms_textView=view.findViewById(R.id.rooms_list_roomNumber);
            linearLayout = view.findViewById(R.id.rooms_list_linearLayout);
        }
    }
    public void showDialog(final Room element) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        dialogBuilder.setTitle("Are you sure want to modify details of "+element.getRoomNumber()+" ?");

        dialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                DatabaseReference databaseReference= new FirebaseRoomHelper().getReference().child(element.getRoomID());
                databaseReference.removeValue();

            }
        });

        dialogBuilder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Room roomd=element;
                Intent intent= new Intent(activity,EditRoomsDetails.class);
                intent.putExtra("ROOMDETAILS", roomd);
                activity.startActivity(intent);

            }

        });

        dialogBuilder.show();
    }
    public void doSomething(ArrayList<Room> r){
        this.items = r;
        this.notifyDataSetChanged();
    }




}
