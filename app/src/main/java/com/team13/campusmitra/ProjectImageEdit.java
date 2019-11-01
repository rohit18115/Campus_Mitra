package com.team13.campusmitra;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.team13.campusmitra.dataholder.Project;
import com.team13.campusmitra.firebaseassistant.FirebaseProjectHelper;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProjectImageEdit extends AppCompatActivity {
    Button update;
    Button delete;
    TextView buffer;
    Project project;
    ProgressBar progressBar;
    private CircleImageView projectImage;
    private static final int PICK_IMAGE=1;
    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_project_image);
        project = (Project) getIntent().getSerializableExtra("Project");
        update = findViewById(R.id.dialog_edit_projectimg_update_btn);
        delete = findViewById(R.id.dialog_edit_projectimg_delete_btn);
        buffer = findViewById(R.id.Add_projimg_Buffer);
        projectImage = findViewById(R.id.edit_img_proj);
        progressBar = findViewById(R.id.proj_img_UPpbar);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                progressBar.setVisibility(View.VISIBLE);

                startActivityForResult(Intent.createChooser(gallery,"Seclect Picture"),PICK_IMAGE);

                finish();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                project.setProjectImageURL("");
                FirebaseProjectHelper helper = new FirebaseProjectHelper();
                helper.updateProject(ProjectImageEdit.this,project);
                finish();
            }
        });

    }
    private String uploadImageToFirebase() {
        final StorageReference ProjectImageREf = FirebaseStorage.getInstance().getReference("ProjectImageRef.jpg");
        String result="";
        if (imageUri != null) {
            ProjectImageREf.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return ProjectImageREf.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri uri = task.getResult();
                        buffer.setText(uri.toString());
                        progressBar.setVisibility(View.GONE);
                        Log.d("URL", "onComplete: " + uri.toString());
                        project.setProjectImageURL(buffer.getText().toString().trim());
                        FirebaseProjectHelper helper = new FirebaseProjectHelper();
                        helper.updateProject(ProjectImageEdit.this,project);
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
                projectImage.setImageBitmap(bitmap);
                uploadImageToFirebase();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }


}
