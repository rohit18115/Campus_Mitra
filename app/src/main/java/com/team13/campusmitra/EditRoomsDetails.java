package com.team13.campusmitra;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.vision.L;
import com.team13.campusmitra.R;
import com.team13.campusmitra.dataholder.Room;

public class EditRoomsDetails extends AppCompatActivity {

    private ImageView room_image;

    private EditText room_number;
    private EditText lab_name;
    private EditText room_type;
    private EditText building_name;
    private EditText system_count;
    private EditText capacity;
    private EditText description;
    private EditText room_notes;
    private Button send;

    private Room room;

    // CHECK

    private int FACULTY = 3;
    private int LAB = 4;

    //::::::::::    INITIALIZING VIEW WIDGETS   ::::::::::

    private void initializer() {

        room_image = findViewById(R.id.edit_room_image);
        room_number = findViewById(R.id.room_number_et);
        lab_name = findViewById(R.id.lab_name_et);
        room_type = findViewById(R.id.room_type_et);
        building_name = findViewById(R.id.building_name_et);
        system_count = findViewById(R.id.system_count_et);
        capacity = findViewById(R.id.capacity_et);
        description = findViewById(R.id.description_et);
        room_notes = findViewById(R.id.room_notes_et);
        send = findViewById(R.id.send_bt);

    }

    //::::::::::    INITIALIZING VIEW WIDGETS   ::::::::::

    private void hideEditText(int roomType) {

        if(roomType == LAB) {
            room_notes.setVisibility(View.GONE);
            room_notes.setText(" ");
        }
        else if(roomType == FACULTY) {
            lab_name.setVisibility(View.GONE);
            lab_name.setText(" ");
            system_count.setVisibility(View.GONE);
            system_count.setText(0);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rooms_details);
        initializer();

        /*
        TODO get room object if required
        TODO pass roomType if new object
        TODO set roomID if new object
         */

        room = new Room();

        int roomType = 0;
        hideEditText(roomType);

        String roomID = "";
        room.setRoomID(roomID);

    }

    //::::::::::    CHECK NO FIELDS ARE EMPTY   ::::::::::

    private boolean checkAllFields() {

        if(room.getRoomID().equals("") || room.getRoomNumber().equals("") || room.getLabName().equals("")
                || room.getRoomBuilding().equals("") || room.getRoomDescription().equals("") || room.getRoomNotes().equals("")) {

            return  false;

        }
        return true;
    }

    //::::::::::    POPULATE ROOM OBJECT   ::::::::::

    private void setRoomDetails() {

        room.setRoomNumber(room_number.getText().toString());
        room.setLabName(lab_name.getText().toString());
        room.setRoomType(Integer.parseInt(room_type.getText().toString()));
        room.setRoomBuilding(building_name.getText().toString());
        room.setSystemCount(Integer.parseInt(system_count.getText().toString()));
        room.setCapacity(Integer.parseInt(capacity.getText().toString()));
        room.setRoomDescription(description.getText().toString());
        room.setRoomNotes(room_notes.getText().toString());

    }



    //::::::::::    ON IMAGE CLICK   ::::::::::

    public void onImageClick(View view) {

        /*
        TODO write what happens when imageView is clicked (upload image/set room.roomImageURL)
        */

        String roomImageURL = "";
        room.setRoomImageURL(roomImageURL);
    }

    //::::::::::    ON BUTTON CLICK   ::::::::::

    public void onSend(View view) {

        setRoomDetails();
        if(checkAllFields()) {

            /*
            TODO send room data to fireBase
             */

        }
        else {
            /*
            TODO form not filled properly. Notify user
             */
        }


    }
}
