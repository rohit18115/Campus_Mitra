package com.team13.campusmitra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.team13.campusmitra.adaptors.BookingListAdaptor;
import com.team13.campusmitra.adaptors.VacantRoomRecyclerViewAdaptor;
import com.team13.campusmitra.dataholder.Booking;
import com.team13.campusmitra.dataholder.Room;
import com.team13.campusmitra.firebaseassistant.FirebaseBookingHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseRoomHelper;

import java.util.ArrayList;

public class BookingListActivity extends AppCompatActivity {
    ArrayList<Booking> bookings;
    ArrayList<Room> rooms;

    String uid;
    RecyclerView recyclerView;
    BookingListAdaptor adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list);
        uid= FirebaseAuth.getInstance().getUid();
        rooms = new ArrayList<>();
        bookings = new ArrayList<>();
        if(uid==null){
            uid="admin";
        }
        recyclerView = findViewById(R.id.booking_list_recyclerView);
        loadData(uid);
    }
    private void loadData(final String uid){
        FirebaseBookingHelper helper = new FirebaseBookingHelper();
        final DatabaseReference reference = helper.getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookings.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Booking b = snapshot.getValue(Booking.class);
                    if(b.getUserID()!=null)
                    if(b.getUserID().toLowerCase().equals(uid.toLowerCase())){
                        bookings.add(b);
                    }
                    else{
                        bookings.add(b);
                    }
                }
                DatabaseReference reference1 = new FirebaseRoomHelper().getReference();
                reference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        rooms.clear();
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Room room = snapshot.getValue(Room.class);
                            rooms.add(room);
                        }
                        loadRecyclerView(rooms,bookings);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadRecyclerView(ArrayList<Room> rooms,ArrayList<Booking> bookings ){
        adaptor = new BookingListAdaptor(rooms,bookings,getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadAdaptorToRecyclerView(recyclerView,adaptor);

    }
    private void loadAdaptorToRecyclerView(RecyclerView recyclerView, BookingListAdaptor adaptor){
        Context context = recyclerView.getContext();
        LayoutAnimationController controller = null;
        controller = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_anim_fall_down);
        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
}
