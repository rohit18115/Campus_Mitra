package com.team13.campusmitra;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.team13.campusmitra.R.array;

import android.widget.AdapterView.OnItemSelectedListener;

import com.team13.campusmitra.dataholder.Room;
import com.team13.campusmitra.firebaseassistant.FirebaseRoomHelper;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public class Rooms extends AppCompatActivity {

    private StorageReference mStorageRef;
    private static final int CHOOSE_IMAGE = 101;
    Spinner room_type_spinner;
    String[] Spinner_Values = {"Discussion Room, Lecture Room, Meeting Room"};
    Button rooms_Done;
    String selectedValue;
    Intent intent;
    EditText editText_rNO;
    EditText roomBuilding;
    Uri uriRoomImage;
    ImageView imageView;
    ProgressBar progressBar;
    EditText RoomNumber;
    EditText roomDescription;
    EditText capacity;
    EditText roomNotes;
    EditText labName;
    EditText systemCount;
    boolean getURl=false;
    Uri ur = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);
        RoomNumber=findViewById(R.id.rooms_number);
        roomDescription=findViewById(R.id.rooms_description);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        editText_rNO = findViewById(R.id.rooms_number);
        roomBuilding = findViewById(R.id.rooms_building);
        progressBar = findViewById(R.id.progressbar);
        capacity=findViewById(R.id.rooms_capacity);
        roomNotes=findViewById(R.id.rooms_notes);
        labName=findViewById(R.id.rooms_lab_names);
        systemCount=findViewById(R.id.rooms_system_count);

        initComponents();
    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Room Image"), CHOOSE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == CHOOSE_IMAGE) && (resultCode == RESULT_OK) && (data != null) && (data.getData() != null)) {
            uriRoomImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriRoomImage);
                imageView.setImageBitmap(bitmap);

                uploadImageToFirebase();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebase() {
        final StorageReference roomImageREf = FirebaseStorage.getInstance().getReference("roomImageREf/" + selectedValue + "/"+RoomNumber+"/Room.jpg");
    getURl = true;
        if (uriRoomImage != null) {
            progressBar.setVisibility(View.VISIBLE);
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
                        Log.d("URL", "onComplete: " + uri.toString());
                    }
                }
            });
        }
    }


    public void initComponents() {
        imageView = findViewById(R.id.image_url);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooser();
            }
        });
        room_type_spinner = findViewById(R.id.type_of_room_spinner);
        rooms_Done = findViewById(R.id.rooms_done);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, array.type_of_room, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        // ArrayAdapter<String>(Rooms.this,R.array.type_of_room, android.R.layout.simple_spinner_dropdown_item );
        room_type_spinner.setAdapter(adapter);
        room_type_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedValue = (String) room_type_spinner.getSelectedItem();
                switch (selectedValue) {

                    case "Lab":
                        labName.setVisibility(View.VISIBLE);
                        systemCount.setVisibility(View.VISIBLE);
                        roomNotes.setVisibility(View.GONE);
                        break;
                    case "Faculty Office":
                        labName.setVisibility(View.GONE);
                        systemCount.setVisibility(View.GONE);
                        roomNotes.setVisibility(View.VISIBLE);
                        break;
                    default:break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        rooms_Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getURl==false){
                if(RoomNumber.getText().toString().isEmpty()||roomBuilding.getText().toString().isEmpty()||room_type_spinner.getSelectedItem().toString().isEmpty()||roomDescription.getText().toString().isEmpty()||capacity.getText().toString().isEmpty()||uriRoomImage.equals(null)) {
                    RoomNumber.setError("Room Number empty");
                    roomBuilding.setError("Room Building empty ");
                    capacity.setError("Capacity empty ");

                }
                else{
                    if(room_type_spinner.getSelectedItemPosition()==0||room_type_spinner.getSelectedItemPosition()==1||room_type_spinner.getSelectedItemPosition()==2) {
                        Room room = new Room();
                        room.setRoomNumber(RoomNumber.getText().toString());
                        room.setRoomBuilding(roomBuilding.getText().toString());
                        room.setRoomType(room_type_spinner.getSelectedItemPosition());
                        room.setRoomImageURL(ur.toString());
                        room.setRoomDescription(roomDescription.getText().toString());
                        room.setCapacity(Integer.parseInt(capacity.getText().toString()));
                        FirebaseRoomHelper helper = new FirebaseRoomHelper();
                        helper.addRoom(getApplicationContext(), room);
                    }
                    else if(room_type_spinner.getSelectedItemPosition()==3) {
                        Room room = new Room();
                        room.setRoomNumber(RoomNumber.getText().toString());
                        room.setRoomBuilding(roomBuilding.getText().toString());
                        room.setRoomType(room_type_spinner.getSelectedItemPosition());
                        room.setRoomImageURL(ur.toString());
                        room.setRoomDescription(roomDescription.getText().toString());
                        room.setCapacity(Integer.parseInt(capacity.getText().toString()));
                        FirebaseRoomHelper helper = new FirebaseRoomHelper();
                        helper.addRoom(getApplicationContext(), room);
                        room.setRoomNotes(roomNotes.getText().toString());

                    }
                    else{
                        Room room = new Room();
                        room.setRoomNumber(RoomNumber.getText().toString());
                        room.setRoomBuilding(roomBuilding.getText().toString());
                        room.setRoomType(room_type_spinner.getSelectedItemPosition());
                        room.setRoomImageURL(ur.toString());
                        room.setRoomDescription(roomDescription.getText().toString());
                        room.setCapacity(Integer.parseInt(capacity.getText().toString()));
                        room.setLabName(labName.getText().toString());
                        room.setSystemCount(Integer.parseInt(systemCount.getText().toString()));
                        FirebaseRoomHelper helper = new FirebaseRoomHelper();
                        helper.addRoom(getApplicationContext(), room);
                    }
                    finish();
                }



            }}
        });

    }
//    private Room getRoomsForDiscussionRoom(){
//        Room room = new Room();
//        room.setRoomNumber(RoomNumber.getText().toString());
//        room.setRoomBuilding(roomBuilding.getText().toString());
//        room.setRoomImageURL(uriRoomImage.toString());
//        room.setCapacity(Integer.parseInt(capacity.getText().toString().trim()));
//        room.setRoomDescription(roomDescription.getText().toString().trim());
//        return room;
//    }
//    private Room getLab(){
//        Room room=  new Room();
//        return room;
//    }


}