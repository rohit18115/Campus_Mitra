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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.team13.campusmitra.dataholder.ResearchLab;
import com.team13.campusmitra.dataholder.Room;
import com.team13.campusmitra.firebaseassistant.FirebaseCoursesHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseResearchLabHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseRoomHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddReasearchLabActivity extends AppCompatActivity implements View.OnClickListener{
    TextInputEditText room_no;
    TextInputEditText room_name;
    TextInputEditText room_proff;
    String mentor_string;
    ArrayList<String> mentors;
    TextView buffer;
    Button add_proj;
    Button done;
    private CircleImageView rlImage;
    private static final int PICK_IMAGE=1;
    Uri imageUri;
    ProgressBar progressBar;
    private void initComponents(){
        room_no = findViewById(R.id.rl_rooms_number);
        room_name = findViewById(R.id.rl_lab_name);
        progressBar = findViewById(R.id.rl_UPpbar);
        room_proff = findViewById(R.id.rl_rooms_professor);
        buffer = findViewById(R.id.Add_rl_Buffer);

    }

    protected void getRLObject(){
        if(checkTextView(room_no)&&checkTextView(room_name)&&checkTextView(room_proff)&& mentors.size()>0){
            ResearchLab rl = new ResearchLab();
            Room r = new Room();
            r.setRoomType(5);
            r.setRoomID(room_no.getText().toString().trim());
            rl.setRoomID(room_no.getText().toString().trim());
            rl.setResearchLabName(room_name.getText().toString().trim());
            mentor_string = room_proff.getText().toString().trim();
            mentors = (ArrayList<String>) Arrays.asList(mentor_string.split(","));
            rl.setMentors(mentors);
            FirebaseResearchLabHelper helper = new FirebaseResearchLabHelper();
            helper.addResearchLab(getApplicationContext(),rl);
            FirebaseRoomHelper helper2 = new FirebaseRoomHelper();
            helper2.addRoom(getApplicationContext(),r);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Enter valid Values!!",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reasearch_lab);
        done = findViewById(R.id.rl_rooms_done);
        done.setOnClickListener(this);
        rlImage = findViewById(R.id.rl_image);
        rlImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                progressBar.setVisibility(View.VISIBLE);

                startActivityForResult(Intent.createChooser(gallery,"Seclect Picture"),PICK_IMAGE);
            }
        });
        initComponents();
    }

    private String uploadImageToFirebase() {
        final StorageReference RLImageREf = FirebaseStorage.getInstance().getReference("RLImageRef.jpg");
        String result="";
        if (imageUri != null) {
            RLImageREf.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return RLImageREf.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri uri = task.getResult();
                        buffer.setText(uri.toString());
                        progressBar.setVisibility(View.GONE);
                        Log.d("URL", "onComplete: " + uri.toString());
                    }
                }
            });
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            imageUri = data.getData();

            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                rlImage.setImageBitmap(bitmap);
                uploadImageToFirebase();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private boolean checkTextView(TextView et){
        if(et.getText().length()<=1){
            et.requestFocus();
            et.setError("Enter Valid values");
            return false;
        }
        return true;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_rooms_done:
                getRLObject();

        }
    }

}
