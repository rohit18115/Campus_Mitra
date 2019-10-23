package com.team13.campusmitra.adaptors;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.firebase.database.DatabaseReference;
import com.team13.campusmitra.AddTimeTableActivity;
import com.team13.campusmitra.R;
import com.team13.campusmitra.dataholder.Course;
import com.team13.campusmitra.dataholder.Room;
import com.team13.campusmitra.dataholder.TimeTableElement;
import com.team13.campusmitra.firebaseassistant.FirebaseTimeTableHelper;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class TimetableRecylerViewAdaptor extends RecyclerView.Adapter<TimetableRecylerViewAdaptor.ViewHolder> {

    private TimeTableElement[] elements;
    private ArrayList<Room> rooms;
    private ArrayList<Course> courses;
    private Context context;
    private AppCompatActivity activity;

    public TimetableRecylerViewAdaptor(TimeTableElement[] elements, ArrayList<Room> rooms, ArrayList<Course> courses, Context context, AppCompatActivity activity) {
        this.elements = elements;
        this.rooms = rooms;
        this.courses = courses;
        this.activity = activity;
        this.context = context;
    }

    public TimetableRecylerViewAdaptor(TimeTableElement[] elements) {
        this.elements = elements;
    }

    public TimetableRecylerViewAdaptor(TimeTableElement[] elements, ArrayList<Room> rooms, ArrayList<Course> courses) {
        this.elements = elements;
        this.rooms = rooms;
        this.courses = courses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View listItem = inflater.inflate(R.layout.recyclerview_timetable, parent, false);
        ViewHolder holder = new ViewHolder(listItem);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final TimeTableElement element = elements[position];
        //System.out.println(element.getCourseID());
        holder.courseId.setText(getCourseCode(element.getCourseID()));
        holder.courseName.setText(getCourseName(element.getCourseID()));
        holder.fromTime.setText(element.getStartTime());
        holder.toTime.setText(element.getEndTime());
        holder.day.setText(element.getDay());
        holder.roomNumber.setText(getRoomNumber(element.getRoomID()));
        addImage(getCourseName(element.getCourseID()), holder.imageView);
        holder.myLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.myLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showEditDialog(element);
                return false;
            }
        });

    }

    private String getCourseName(String key) {
        for (Course c : courses) {
            if (c.getCourseID().equals(key)) {
                return c.getCourseName();
            }
        }
        return "";
    }

    private String getCourseCode(String key) {
        for (Course c : courses) {
            if (c.getCourseID().equals(key)) {
                return c.getCourseCode();
            }
        }
        return "";
    }

    private String getRoomNumber(String key) {
        for (Room c : rooms) {
            if (c.getRoomID().equals(key)) {
                return c.getRoomNumber();
            }
        }
        return "";
    }

    @Override
    public int getItemCount() {
        return elements.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView courseId;
        public TextView courseName;
        public TextView fromTime;
        public TextView toTime;
        public TextView day;
        public TextView roomNumber;
        public RelativeLayout myLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.tt_element_image);
            this.courseId = itemView.findViewById(R.id.tt_element_courseid);
            this.courseName = itemView.findViewById(R.id.tt_element_courseName);
            this.fromTime = itemView.findViewById(R.id.tt_element_fromTime);
            this.toTime = itemView.findViewById(R.id.tt_element_toTime);
            this.day = itemView.findViewById(R.id.tt_element_Day);
            this.roomNumber = itemView.findViewById(R.id.tt_element_roomNo);
            this.myLayout = itemView.findViewById(R.id.tt_element_rl);
        }
    }

    public void addImage(String str, ImageView civ) {
        String input = str.toUpperCase();
        char first = input.charAt(0);
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        TextDrawable drawable1 = TextDrawable.builder()
                .buildRoundRect(Character.toString(first), color, 15); // radius in px
        TextDrawable drawable2 = TextDrawable.builder()
                .buildRound(Character.toString(first), color);
        civ.setImageDrawable(drawable2);

    }

    public void filter(TimeTableElement[] e) {
        this.elements = e;
        this.notifyDataSetChanged();

    }

    private void showEditDialog(final TimeTableElement element) {
        TimeTableElement newElement = new TimeTableElement();
        final AlertDialog dialog;
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_tt_update, null);
        final LinearLayout ll1, ll2, ll3;
        ll1 = view.findViewById(R.id.dialog_tt_room_ll);
        ll2 = view.findViewById(R.id.dialog_tt_room_ll2);
        ll3 = view.findViewById(R.id.dialog_tt_room_ll3);

        final Spinner roomSpinner = view.findViewById(R.id.dialog_tt_room_spinner);
        final Spinner daySpinner = view.findViewById(R.id.dialog_tt_day_spinner);
        final Spinner actionSpinner = view.findViewById(R.id.dialog_tt_action_spinner);

        Button update = view.findViewById(R.id.dialog_tt_room_btn_update);
        Button delete = view.findViewById(R.id.dialog_tt_room_btn_del);


        final TextView timeTv = view.findViewById(R.id.dialog_tt_room_et);

        loadDataInSpinner(actionSpinner, getActions());
        loadDataInSpinner(daySpinner, getDays());
        loadDataInSpinner(roomSpinner, getRoomsList());

        alertDialog.setView(view);
        alertDialog.setTitle("Edit Timetable Record");
        dialog= alertDialog.create();
        dialog.show();
        actionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        break;
                    case 1:
                        ll1.setVisibility(View.VISIBLE);
                        ll2.setVisibility(View.GONE);
                        ll3.setVisibility(View.GONE);
                        break;
                    case 2:

                    case 3:

                        ll1.setVisibility(View.GONE);
                        ll2.setVisibility(View.VISIBLE);
                        ll3.setVisibility(View.GONE);
                        break;
                    case 4:

                        ll1.setVisibility(View.GONE);
                        ll2.setVisibility(View.GONE);
                        ll3.setVisibility(View.VISIBLE);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = actionSpinner.getSelectedItemPosition();
                switch (i){
                    case 0:
                        break;
                    case 1:
                        element.setRoomID(rooms.get(roomSpinner.getSelectedItemPosition()).getRoomID());
                        break;
                    case 2:
                        if(timeTv.getText().toString().contains(":"))
                            element.setStartTime(timeTv.getText().toString());
                        else
                            Toast.makeText(context,"ENTER VALID TIME",Toast.LENGTH_LONG).show();
                        break;
                    case 3:

                        if(timeTv.getText().toString().contains(":"))
                            element.setEndTime(timeTv.getText().toString());
                        else
                            Toast.makeText(context,"ENTER VALID TIME",Toast.LENGTH_LONG).show();
                        break;
                    case 4:
                        element.setDay((String) daySpinner.getSelectedItem());
                        break;
                }
                FirebaseTimeTableHelper helper = new FirebaseTimeTableHelper();
                helper.updateTimeTable(context,element);
                dialog.cancel();

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(element);
                dialog.cancel();
            }
        });

        timeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTime(timeTv);
            }
        });
    }

    private void loadDataInSpinner(Spinner daySpinner, ArrayList<String> data) {

        ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, data);
        areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(areasAdapter);


    }

    private ArrayList<String> getRoomsList() {
        ArrayList<String> roomsList = new ArrayList<>();
        for (Room r : rooms) {
            roomsList.add(r.getRoomNumber());
        }
        return roomsList;
    }

    private ArrayList<String> getDays() {
        ArrayList<String> ar = new ArrayList<>();
        ar.add("Sunday");
        ar.add("Monday");
        ar.add("Tuesday");
        ar.add("Wednesday");
        ar.add("Thursday");
        ar.add("Friday");
        ar.add("Saturday");
        return ar;


    }

    private String getTime(final TextView textView) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String minut = "";
                if (selectedMinute < 10) {
                    minut = "0" + selectedMinute;
                } else {
                    minut = "" + selectedMinute;
                }
                textView.setText(selectedHour + ":" + minut);
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

        return "";
    }

    private ArrayList<String> getActions() {
        ArrayList<String> arr = new ArrayList<>();
        arr.add("None");
        arr.add("Edit Rooms");
        arr.add("Edit Start time");
        arr.add("Edit End time");
        arr.add("Edit day");
        return arr;
    }

    public void showDialog(final TimeTableElement element) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle("Are you sure want to delete ?");

        dialogBuilder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseTimeTableHelper helper = new FirebaseTimeTableHelper();
                DatabaseReference ref = helper.getReference().child(element.getTimeTableID());
                ref.removeValue();


            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }

        });

        dialogBuilder.show();
    }
}

