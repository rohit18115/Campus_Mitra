package com.team13.campusmitra;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.team13.campusmitra.Utils.LetterImageView;

import java.util.ArrayList;
import java.util.List;

public class FacultyProfile extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {
    Button setOfficeHours, courseTaken;
    private ListView listView;
    public static SharedPreferences sharedPreferences;
    public static String SEL_DAY;
    private Toolbar toolbar;
    AlertDialog dialog,dialog1;
    TextView Day;
    TextView venue;
    WeekAdapter venueadapter;
    TextView Time;
    int num=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_profile);
        Day = findViewById(R.id.FPTVday);
        setOfficeHours = findViewById(R.id.setOfficeHours);
        courseTaken = findViewById(R.id.FPCourseTaken);
        setOfficeHours.setOnClickListener(this);
        courseTaken.setOnClickListener(this);
        initToolbar();
        setupUIViews();
        setupListView();
        AlertDialog.Builder builder = new AlertDialog.Builder(FacultyProfile.this);
        builder.setCancelable(true);
        builder.setView(listView);
        dialog = builder.create();

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        num=num+1;
        TextView startTime = (TextView)findViewById(R.id.FPTVStartTime);
        TextView endTime = (TextView)findViewById(R.id.FPTVEndTime);

        if(num%2!=0)
            startTime.setText(hourOfDay+":"+minute);
        else
            endTime.setText(hourOfDay+":"+minute);
    }

    private void setupUIViews(){
        listView = new ListView(this);
        sharedPreferences = getSharedPreferences("MY_DAY", MODE_PRIVATE);
    }
    private void initToolbar(){
        getSupportActionBar().setTitle("Week");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void setupListView() {
        String[] week = getResources().getStringArray(R.array.Week);
        final WeekAdapter adapter = new WeekAdapter(this, R.layout.activity_office_hours_day_single_item, week);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //adapter.getItem(position).toString()
                Day.setText(adapter.getItem(position).toString());
                dialog.dismiss();
                DialogFragment startTimePicker = new TimePickerFragment();
                startTimePicker.show(getSupportFragmentManager(), "Start Time");
                DialogFragment endTimePicker = new TimePickerFragment();
                endTimePicker.show(getSupportFragmentManager(), "End Time");
                ListView venueList = new ListView(FacultyProfile.this);
                String[] venueData = getResources().getStringArray(R.array.Venue);
                venueadapter = new WeekAdapter(FacultyProfile.this, R.layout.activity_office_hours_day_single_item, venueData);
                venueList.setAdapter(venueadapter);
                AlertDialog.Builder builder1 = new AlertDialog.Builder(FacultyProfile.this);
                builder1.setCancelable(true);
                builder1.setView(venueList);
                dialog1 = builder1.create();
                dialog1.show();
                venue = (TextView)findViewById(R.id.FPTVVenue);
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


        public class WeekAdapter extends ArrayAdapter {

            private int resource;
            private LayoutInflater layoutInflater;
            private String[] week = new String[]{};
            private String retDay = "";

            public WeekAdapter(FacultyProfile context, int resource, String[] objects) {
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
                    holder.ivLogo = (LetterImageView)convertView.findViewById(R.id.OHDSILetter);
                    holder.tvWeek = (TextView)convertView.findViewById(R.id.OHDSItv);
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
        public boolean onOptionsItemSelected(MenuItem item) {

            switch(item.getItemId()){
                case android.R.id.home : {
                    onBackPressed();
                }
            }
            return super.onOptionsItemSelected(item);
        }



    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.setOfficeHours :
                dialog.show();

            case R.id.FPCourseTaken:
                Intent intent1 = new Intent(FacultyProfile.this, FacultyCourseTakenRecyclerView.class);
                startActivity(intent1);


        }
    }
}
