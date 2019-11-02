package com.team13.campusmitra;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.team13.campusmitra.Utils.LetterImageView;
import com.team13.campusmitra.dataholder.Faculty;
import com.team13.campusmitra.dataholder.OfficeHours;
import com.team13.campusmitra.dataholder.Room;
import com.team13.campusmitra.dataholder.User;
import com.team13.campusmitra.firebaseassistant.FirebaseFacultyHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseRoomHelper;

import java.util.ArrayList;

public class ChangeOfficeHours extends AppCompatActivity implements View.OnClickListener ,TimePickerDialog.OnTimeSetListener  {

    TextView day, sTime, eTime, venue;
    Button setOfficeHours;
    private ListView listView,listViewRoom;
    WeekAdapter venueadapter;
    AlertDialog dialog, dialog1;
    String[] venueData;
    ArrayList<Room> rooms = new ArrayList<>();
    ListView venueList;
    int num = 0;
    User user;
    Faculty faculty;

    protected void loadData() {


        final String userId = user.getUserId();
        FirebaseFacultyHelper helper2 = new FirebaseFacultyHelper();
        DatabaseReference reference2 = helper2.getReference().child(userId);
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                faculty = dataSnapshot.getValue(Faculty.class);
                OfficeHours of = faculty.getOfficeHours();
                if(of!=null) {
                    day.setText(of.getDay());
                    eTime.setText(of.getEndTime());
                    sTime.setText(of.getStartTime());
                    venue.setText(of.getVenue());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


    }

    protected void uploadData() {
        OfficeHours of = new OfficeHours();
        of.setDay(day.getText().toString());
        of.setVenue(venue.getText().toString());
        of.setStartTime(sTime.getText().toString());
        of.setEndTime(eTime.getText().toString());
        faculty.setOfficeHours(of);
        FirebaseFacultyHelper helper2 = new FirebaseFacultyHelper();
        helper2.addFaculty(getApplicationContext(),faculty);
    }

    private void setupUIViews(){
        listView = new ListView(this);
        listViewRoom = new ListView(this);
        setOfficeHours = findViewById(R.id.set_off_hours);
        setOfficeHours.setOnClickListener(this);
        day = findViewById(R.id.coh_day);
        sTime = findViewById(R.id.coh_stime);
        eTime = findViewById(R.id.coh_etime);
        venue = findViewById(R.id.coh_venue);
        DatabaseReference roomReference = new FirebaseRoomHelper().getReference();
        roomReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rooms.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Room room = snapshot.getValue(Room.class);
                    rooms.add(room);

                }
                venueData = new String[rooms.size()];
                for(int i = 0;i<rooms.size();i++) {
                    venueData[i] = rooms.get(i).getRoomNumber();
                }
                venueList = new ListView(ChangeOfficeHours.this);
                venueadapter = new WeekAdapter(getApplicationContext(), R.layout.activity_office_hours_day_single_item,venueData);
                venueList.setAdapter(venueadapter);
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ChangeOfficeHours.this);
                builder1.setCancelable(true);
                builder1.setView(venueList);
                dialog1 = builder1.create();
                //.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_office_hours);
        user  = (User) getIntent().getSerializableExtra("MYKEY");
        //System.out.println("abcd"+user);
        setupUIViews();
        setupListView();
        loadData();
        AlertDialog.Builder builder = new AlertDialog.Builder(ChangeOfficeHours.this);
        builder.setCancelable(true);
        builder.setView(listView);
        dialog = builder.create();
    }

    private void setupListView() {

        String[] week = getResources().getStringArray(R.array.Week);
        final WeekAdapter adapter = new WeekAdapter(this, R.layout.activity_office_hours_day_single_item, week);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //adapter.getItem(position).toString()
                day.setText(adapter.getItem(position).toString());
                dialog.dismiss();
                DialogFragment startTimePicker = new TimePickerFragment();
                startTimePicker.show(getSupportFragmentManager(), "Start Time");
                DialogFragment endTimePicker = new TimePickerFragment();
                endTimePicker.show(getSupportFragmentManager(), "End Time");
                dialog1.show();
                venueList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        venue.setText(venueadapter.getItem(position).toString());
                        dialog1.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        num=num+1;
        Log.d("lolo", "num"+num);
        if(num%2!=0)
            sTime.setText(hourOfDay+":"+minute);
        else
            eTime.setText(hourOfDay+":"+minute);
    }


    public class WeekAdapter extends ArrayAdapter {

        private int resource;
        private LayoutInflater layoutInflater;
        private String[] week = new String[]{};
        private String retDay = "";

        public WeekAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);
            this.resource = resource;
            this.week = objects;
            layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            WeekAdapter.ViewHolder holder;
            if(convertView == null){
                holder = new WeekAdapter.ViewHolder();
                convertView = layoutInflater.inflate(resource, null);
                holder.ivLogo = convertView.findViewById(R.id.OHDSILetter);
                holder.tvWeek = convertView.findViewById(R.id.OHDSItv);
                convertView.setTag(holder);
            }else{
                holder = (WeekAdapter.ViewHolder)convertView.getTag();
            }

            holder.ivLogo.setOval(true);
            holder.ivLogo.setLetter(week[position].charAt(0));
            holder.tvWeek.setText(week[position]);

            return convertView;
        }


        class ViewHolder{
            private LetterImageView ivLogo;
            private TextView tvWeek;
        }
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.set_off_hours) {
            dialog.show();
            uploadData();
        }
    }
}
