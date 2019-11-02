package com.team13.campusmitra;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.L;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.team13.campusmitra.R;
import com.team13.campusmitra.dataholder.Room;
import com.team13.campusmitra.firebaseassistant.FirebaseRoomHelper;

import java.io.IOException;

public class EditRoomsDetails extends AppCompatActivity {

    private ImageView room_image;
    Spinner room_type_spinner;
    String[] Spinner_Values = {"Discussion Room, Lecture Room, Meeting Room"};
    private EditText room_number;
    private EditText lab_name;
    private Spinner room_type;
    private EditText building_name;
    private EditText system_count;
    private EditText capacity;
    private EditText description;
    private EditText room_notes;
    private Button send;
    boolean getURl=false;
    Uri ur = null;
    private Room room;
    Uri uriRoomImage;
    String selectedValue;

    // CHECK

    private int FACULTY = 3;
    private int LAB = 4;
    private static final int CHOOSE_IMAGE = 101;

    //::::::::::    INITIALIZING VIEW WIDGETS   ::::::::::

    private void initializer(Room room) {

        room_image = findViewById(R.id.edit_room_image);
        room_type_spinner = findViewById(R.id.spinner_room_type);
        room_number = findViewById(R.id.room_number_et);
        lab_name = findViewById(R.id.lab_name_et);
        //Toast.makeText(getApplicationContext(),""+room.getRoomType(),Toast.LENGTH_LONG).show();
        room_notes = findViewById(R.id.room_notes_et);
        building_name = findViewById(R.id.building_name_et);
        capacity = findViewById(R.id.capacity_et);
        description = findViewById(R.id.description_et);
        send = findViewById(R.id.send_bt);
        system_count = findViewById(R.id.system_count_et);
        room_number.setText(room.getRoomNumber());
        building_name.setText(room.getRoomBuilding());
        capacity.setText(String.valueOf(room.getCapacity()));

        Glide.with(this)
                .asBitmap()
                .load(room.getRoomImageURL())
                .placeholder(R.drawable.classroom)
                .into(room_image);

        if(room.getRoomType()==LAB)
        {
            lab_name.setText(room.getLabName());
            system_count.setText(String.valueOf(room.getSystemCount()));
            room_notes.setVisibility(View.GONE);
        }
        else if(room.getRoomType()==FACULTY)
        {
            system_count.setVisibility(View.GONE);
            lab_name.setVisibility(View.GONE);
            room_notes.setText(room.getRoomNotes());
        }
        else
        {
            lab_name.setVisibility(View.GONE);
            room_notes.setVisibility(View.GONE);
            system_count.setVisibility(View.GONE);
        }
        description.setText(room.getRoomDescription());


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
            system_count.setText(0);
            system_count.setVisibility(View.GONE);

        }

    }
    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Room Image"), CHOOSE_IMAGE);
    }

    private boolean imageUpdateFlag = false;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == CHOOSE_IMAGE) && (resultCode == RESULT_OK) && (data != null) && (data.getData() != null)) {
            uriRoomImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriRoomImage);
                room_image.setImageBitmap(bitmap);

                uploadImageToFirebase();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void uploadImageToFirebase() {
        final StorageReference roomImageREf = FirebaseStorage.getInstance().getReference("roomImageREf/" + selectedValue + "/"+room_number+"/Room.jpg");
        getURl = true;
        if (uriRoomImage != null) {
            //progressBar.setVisibility(View.VISIBLE);
            roomImageREf.putFile(uriRoomImage).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return roomImageREf.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri uri = task.getResult();
                        ur = uri;
                        getURl=false;
                        imageUpdateFlag = true;
                        Log.d("URL", "onComplete: " + uri.toString());
                    }
                }
            });
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rooms_details);
        room = (Room) getIntent().getSerializableExtra("ROOMDETAILS");

        //Log.d("ERD",room.getRoomBuilding());
        initializer(room);

        room_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooser();
            }
        });
        int roomType = room.getRoomType();
        //hideEditText(roomType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.type_of_room, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // ArrayAdapter<String>(Rooms.this,R.array.type_of_room, android.R.layout.simple_spinner_dropdown_item );
        room_type_spinner.setAdapter(adapter);
        room_type_spinner.setSelection(room.getRoomType());
        room_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedValue = (String) room_type_spinner.getSelectedItem();
                int val=(int)room_type_spinner.getSelectedItemId();
                Log.d("selected",String.valueOf(val));
                //hideEditText((int)room_type_spinner.getSelectedItemId());
                if(val==LAB)
                {
                    //system_count = findViewById(R.id.system_count_et);
                    system_count.setVisibility(View.VISIBLE);
                    //lab_name.setText(room.getLabName());
                    lab_name.setVisibility(View.VISIBLE);
                    room_notes.setVisibility(View.GONE);
                }
                else if(val==FACULTY)
                {
                    system_count.setVisibility(View.GONE);
                    lab_name.setVisibility(View.GONE);
                    room_notes.setVisibility(View.VISIBLE);
                }

                else
                {
                    lab_name.setVisibility(View.GONE);
                    room_notes.setVisibility(View.GONE);
                    system_count.setVisibility(View.GONE);
                }

            }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub

        }
    });


                //Toast.makeText(this, room.getRoomID(), Toast.LENGTH_SHORT).show();
    }

    //::::::::::    CHECK NO FIELDS ARE EMPTY   ::::::::::

    private boolean checkAllFields() {

        if(room_number.getText().toString().isEmpty() || building_name.getText().toString().isEmpty() || capacity.getText().toString().isEmpty()
                || description.getText().toString().isEmpty() ) {

                room_number.setError("Room Number Empty");
                building_name.setError("Room Building Empty");
                capacity.setError("Capacity cannot be empty");
            return  false;

        }
        return true;
    }

    //::::::::::    POPULATE ROOM OBJECT   ::::::::::

    private void setRoomDetails() {

//        checkAllFields();
        room.setRoomNumber(room_number.getText().toString());
        room.setLabName(lab_name.getText().toString());
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


        if(checkAllFields()) {

            //setRoomDetails();
            if(room_type_spinner.getSelectedItemPosition()==0||room_type_spinner.getSelectedItemPosition()==1||room_type_spinner.getSelectedItemPosition()==2) {
                room.setRoomNumber(room_number.getText().toString());
                room.setRoomBuilding(building_name.getText().toString());
                room.setRoomType(room_type_spinner.getSelectedItemPosition());
                if(imageUpdateFlag)
                    room.setRoomImageURL(ur.toString());
                room.setRoomDescription(description.getText().toString());
                room.setCapacity(Integer.parseInt(capacity.getText().toString()));
                FirebaseRoomHelper helper = new FirebaseRoomHelper();
                helper.updateRoom(getApplicationContext(), room);
            }
            else if(room_type_spinner.getSelectedItemPosition()==3) {
                Toast.makeText(getApplicationContext(),room_type_spinner.getSelectedItemPosition()+"",Toast.LENGTH_LONG).show();
                room.setRoomNumber(room_number.getText().toString());
                room.setRoomBuilding(building_name.getText().toString());
                room.setRoomType(room_type_spinner.getSelectedItemPosition());
                if(imageUpdateFlag)
                room.setRoomImageURL(ur.toString());
                room.setRoomDescription(description.getText().toString());
                room.setCapacity(Integer.parseInt(capacity.getText().toString()));

                room.setRoomNotes(room_notes.getText().toString());
                FirebaseRoomHelper helper = new FirebaseRoomHelper();
                helper.updateRoom(getApplicationContext(), room);
            }
            else{
                room.setRoomNumber(room_number.getText().toString());
                room.setRoomBuilding(building_name.getText().toString());
                room.setRoomType(room_type_spinner.getSelectedItemPosition());
                if(imageUpdateFlag)
                    room.setRoomImageURL(ur.toString());

                room.setRoomDescription(description.getText().toString());
                room.setCapacity(Integer.parseInt(capacity.getText().toString()));
                room.setLabName(lab_name.getText().toString());
                room.setSystemCount(Integer.parseInt(system_count.getText().toString()));
                FirebaseRoomHelper helper = new FirebaseRoomHelper();
                helper.updateRoom(getApplicationContext(), room);
            }

        }
        else {

            Toast.makeText(this, "Invalid Details!", Toast.LENGTH_SHORT).show();
        }


    }
}
