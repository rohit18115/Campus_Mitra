package com.team13.campusmitra.fragments;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.team13.campusmitra.FacultyCourseTakenRecyclerView;
import com.team13.campusmitra.R;
import com.team13.campusmitra.TimePickerFragment;
import com.team13.campusmitra.Utils.LetterImageView;
import com.team13.campusmitra.dataholder.Faculty;
import com.team13.campusmitra.dataholder.OfficeHours;
import com.team13.campusmitra.firebaseassistant.FirebaseFacultyHelper;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class FacultyProfileFragment extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {
    FloatingActionButton fab;
    private static final int PICK_CONTACT_REQUEST = 1 ;
    WeekAdapter venueadapter;
    Animation rotateForward, rotateBackward;
    AppCompatTextView designation;
    AppCompatTextView department;
    AppCompatTextView officeHours;
    AppCompatTextView coursesTaken;
    AppCompatTextView domains;
    AppCompatTextView roomNo;
    private ListView listView,listViewDept,listViewRoom,listViewDesignation;
    AlertDialog dialog,dialog1,dialogDept,dialogRoom,dialogDesig;
    String startTime ="";
    String endTime = "";
    String venue = "";
    int num=0;
    ArrayList<String> selected;
    //    TextInputEditText ETFName;
    TextInputEditText ETDomain;
//    TextInputEditText ETUname;
//    TextInputEditText ETOEmail;
    ProgressBar pb;
    public static SharedPreferences sharedPreferences;


    private void setupUIViews(View view){
        listView = new ListView(view.getContext());
        listViewDept = new ListView(view.getContext());
        listViewRoom = new ListView(view.getContext());
        listViewDesignation = new ListView(view.getContext());
        ETDomain = new TextInputEditText(view.getContext());

    }

    protected void initComponent(View view) {
        designation = view.findViewById(R.id.ffp_desig);
        department = view.findViewById(R.id.ffp_dept);
        roomNo = view.findViewById(R.id.ffp_room);
        officeHours = view.findViewById(R.id.ffp_office_hours);
        coursesTaken = view.findViewById(R.id.ffp_course_taken);
        domains = view.findViewById(R.id.ffp_domains);
        pb = view.findViewById(R.id.ffp_pb);
        fab = view.findViewById(R.id.ffp_fab);
        selected = new ArrayList<>();
    }

    protected void loadData(View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        final String uid = auth.getCurrentUser().getUid();
        FirebaseFacultyHelper helper = new FirebaseFacultyHelper();
        DatabaseReference reference = helper.getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (getActivity() == null) {
                    return;
                }
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Faculty fac = snapshot.getValue(Faculty.class);
                    if (fac.getUserID().equals(uid)) {
                        Log.d("lololo", "onDataChange: " + fac.getRoomid());
                        designation.setText(fac.getDesignation());
                        department.setText(fac.getDepartment());
                        roomNo.setText(fac.getRoomNo());
                        OfficeHours of = fac.getOfficeHours();
                        if(of!=null) {
                            String set = of.getDay() + " from " + of.getStartTime() + " to " + of.getEndTime() + " at " + of.getVenue();
                            officeHours.setText(set);
                        }
                        String cor = fac.getDomains();
                        if(cor!=null && !cor.isEmpty())
                            domains.setText(cor);
                        ArrayList<String> s = fac.getCoursesTaken();
                        cor = "";
                        if(s!=null) {
                            for (int i = 0; i < s.size(); i++) {
                                cor = cor + s.get(i) + "\n";
                            }
                            coursesTaken.setText(cor);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    private void animateFab() {
        fab.startAnimation(rotateForward);
        fab.startAnimation(rotateBackward);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faculty_profile, container, false);

        initComponent(view);
        setupUIViews(view);
        setupListView(view);
        loadData(view);
        rotateForward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_backward);
        fab.setOnClickListener(this);
        designation.setOnClickListener(this);
        department.setOnClickListener(this);
        roomNo.setOnClickListener(this);
        officeHours.setOnClickListener(this);
        coursesTaken.setOnClickListener(this);
        domains.setOnClickListener(this);
        designation.setEnabled(false);
        department.setEnabled(false);
        roomNo.setEnabled(false);
        officeHours.setEnabled(false);
        coursesTaken.setEnabled(false);
        domains.setEnabled(false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setView(listView);
        dialog = builder.create();
        AlertDialog.Builder builderDept = new AlertDialog.Builder(getActivity());
        builderDept.setCancelable(true);
        builderDept.setView(listViewDept);
        dialogDept = builderDept.create();
        AlertDialog.Builder builderRoom = new AlertDialog.Builder(getActivity());
        builderRoom.setCancelable(true);
        builderRoom.setView(listViewRoom);
        dialogRoom = builderRoom.create();
        AlertDialog.Builder builderDesig = new AlertDialog.Builder(getActivity());
        builderDesig.setCancelable(true);
        builderDesig.setView(listViewDesignation);
        dialogDesig = builderDesig.create();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ffp_fab:
                animateFab();
                fab.setImageResource(R.drawable.ic_check);
                Snackbar.make(v, "Click on each component to edit.", Snackbar.LENGTH_LONG).setTextColor(Color.WHITE)
                        .setAction("Action", null).show();
                //Toast.makeText(getActivity(),"Click on each component to edit.",Toast.LENGTH_LONG).show();
                designation.setEnabled(true);
                department.setEnabled(true);
                roomNo.setEnabled(true);
                officeHours.setEnabled(true);
                coursesTaken.setEnabled(true);
                domains.setEnabled(true);


                break;
            case R.id.ffp_desig:
                designation.setEnabled(true);
                department.setEnabled(true);
                roomNo.setEnabled(true);
                officeHours.setEnabled(true);
                coursesTaken.setEnabled(true);
                domains.setEnabled(true);
                dialogDesig.show();



                break;
            case R.id.ffp_dept:

                designation.setEnabled(true);
                department.setEnabled(true);
                roomNo.setEnabled(true);
                officeHours.setEnabled(true);
                coursesTaken.setEnabled(true);
                domains.setEnabled(true);
                dialogDept.show();


                break;
            case R.id.ffp_room:
                designation.setEnabled(true);
                department.setEnabled(true);
                roomNo.setEnabled(true);
                officeHours.setEnabled(true);
                coursesTaken.setEnabled(true);
                domains.setEnabled(true);
                dialogRoom.show();

                break;
            case R.id.ffp_office_hours:
                designation.setEnabled(true);
                department.setEnabled(true);
                roomNo.setEnabled(true);
                officeHours.setEnabled(true);
                coursesTaken.setEnabled(true);
                domains.setEnabled(true);
                dialog.show();


                break;
            case R.id.ffp_domains:
                designation.setEnabled(true);
                department.setEnabled(true);
                roomNo.setEnabled(true);
                officeHours.setEnabled(true);
                coursesTaken.setEnabled(true);
                domains.setEnabled(true);
                android.app.AlertDialog.Builder builderDomain = new android.app.AlertDialog.Builder(v.getContext());
                builderDomain.setTitle(" User Name");
                builderDomain.setView(ETDomain);
                builderDomain.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        domains.setText(ETDomain.getEditableText().toString());
                        Toast.makeText(getActivity(),"Name has been successfully changed",Toast.LENGTH_LONG).show();
                    }
                });

                builderDomain.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                if(ETDomain.getParent() != null) {
                    ((ViewGroup)ETDomain.getParent()).removeView(ETDomain); // <- fix


                }
                builderDomain.show();


                break;
            case R.id.ffp_course_taken:
                designation.setEnabled(true);
                department.setEnabled(true);
                roomNo.setEnabled(true);
                officeHours.setEnabled(true);
                coursesTaken.setEnabled(true);
                domains.setEnabled(true);
                Intent intent1 = new Intent(v.getContext(), FacultyCourseTakenRecyclerView.class);
                startActivityForResult(intent1, PICK_CONTACT_REQUEST);


                break;




        }
    }
    private void setupListView(View view) {
        String[] week = getResources().getStringArray(R.array.Week);
        final WeekAdapter adapter = new WeekAdapter(view.getContext(), R.layout.activity_office_hours_day_single_item, week);
        listView.setAdapter(adapter);
        String[] option = getResources().getStringArray(R.array.Dept);
        final WeekAdapter adapterDept = new WeekAdapter(view.getContext(), R.layout.activity_office_hours_day_single_item, option);
        listViewDept.setAdapter(adapterDept);
        String[] optionroom = getResources().getStringArray(R.array.Venue);
        final WeekAdapter adapterRoom = new WeekAdapter(view.getContext(), R.layout.activity_office_hours_day_single_item, optionroom);
        listViewRoom.setAdapter(adapterRoom);
        String[] optionDesig = getResources().getStringArray(R.array.Designation);
        final WeekAdapter adapterDesig = new WeekAdapter(view.getContext(), R.layout.activity_office_hours_day_single_item, optionDesig);
        listViewDesignation.setAdapter(adapterDesig);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //adapter.getItem(position).toString()
                final String Day=(adapter.getItem(position).toString());
                dialog.dismiss();
                DialogFragment startTimePicker = new TimePickerFragment();
                startTimePicker.show(getActivity().getSupportFragmentManager(), "Start Time");
                DialogFragment endTimePicker = new TimePickerFragment();
                endTimePicker.show(getActivity().getSupportFragmentManager(), "End Time");
                ListView venueList = new ListView(view.getContext());
                String[] venueData = getResources().getStringArray(R.array.Venue);
                venueadapter = new WeekAdapter(view.getContext(), R.layout.activity_office_hours_day_single_item, venueData);
                venueList.setAdapter(venueadapter);
                AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
                builder1.setCancelable(true);
                builder1.setView(venueList);
                dialog1 = builder1.create();
                dialog1.show();
                venueList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        venue= (venueadapter.getItem(position).toString());
                        officeHours.setText(Day+" "+startTime+"-"+endTime+" "+venue);
                        dialog1.dismiss();
                    }
                });


            }

        });
        listViewDept.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    //adapter.getItem(position).toString()
                                                    department.setText(adapterDept.getItem(position).toString());
                                                    dialogDept.dismiss();


                                                }

                                            }

        );
        listViewRoom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    //adapter.getItem(position).toString()
                                                    roomNo.setText(adapterRoom.getItem(position).toString());
                                                    dialogRoom.dismiss();


                                                }

                                            }

        );
        listViewDesignation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                       @Override
                                                       public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                           //adapter.getItem(position).toString()
                                                           designation.setText(adapterDesig.getItem(position).toString());
                                                           dialogDesig.dismiss();


                                                       }

                                                   }

        );
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check for the results
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bundle args;
                if(data!=null) {
                    args = data.getBundleExtra("selected_course_Name");
                    selected = (ArrayList<String>)args.getSerializable("ARRAYLIST");
                }
                if(selected.size()!=0) {
                    String t = " ";
                    for(int i =0;i<selected.size();i++) {
                        Log.d("lolo", "onActivityResult: add to t");
                        t = t + selected.get(i) + " ";
                    }
                    coursesTaken.setText(t);
                }
            }
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case android.R.id.home : {
                getView().setFocusableInTouchMode(true);
                getView().setOnKeyListener( new View.OnKeyListener(){
                    @Override
                    public boolean onKey( View v, int keyCode, KeyEvent event ){
                        if( keyCode == KeyEvent.KEYCODE_BACK ){
                            return true;
                        }
                        return false;
                    }
                } );
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        num=num+1;
        Log.d("num","hello");

        if(num%2!=0)
            startTime=(hourOfDay+":"+minute);
        else
            endTime=(hourOfDay+":"+minute);
    }

}
