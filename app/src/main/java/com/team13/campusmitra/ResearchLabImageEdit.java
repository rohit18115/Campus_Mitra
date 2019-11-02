package com.team13.campusmitra;

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
import com.team13.campusmitra.dataholder.ResearchLab;
import com.team13.campusmitra.firebaseassistant.FirebaseProjectHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseResearchLabHelper;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ResearchLabImageEdit extends AppCompatActivity{

    Button update;
    Button delete;
    TextView buffer;
    TextView click_to_choose;
    ResearchLab researchLab;
    ProgressBar progressBar;
    private CircleImageView rlImage;
    private static final int PICK_IMAGE=1;
    Intent intent;
    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_rlab_image_edit);
        researchLab = (ResearchLab) getIntent().getSerializableExtra("Reseach Lab");
        update = findViewById(R.id.dialog_edit_rlimg_update_btn);
        click_to_choose = findViewById(R.id.update_rl_img);
        delete = findViewById(R.id.dialog_edit_rlimg_delete_btn);
        buffer = findViewById(R.id.Add_rlimg_Buffer);
        rlImage = findViewById(R.id.edit_img_rl);
        progressBar = findViewById(R.id.rl_img_UPpbar);
        click_to_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                progressBar.setVisibility(View.VISIBLE);
                startActivityForResult(Intent.createChooser(gallery,"Seclect Picture"),PICK_IMAGE);
                rlImage.setVisibility(View.VISIBLE);

            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseResearchLabHelper helper = new FirebaseResearchLabHelper();
                helper.updateReseachLab(ResearchLabImageEdit.this, researchLab);
                finish();

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                researchLab.setImageURL("");
                FirebaseResearchLabHelper helper = new FirebaseResearchLabHelper();
                helper.updateReseachLab(ResearchLabImageEdit.this, researchLab);
                finish();
            }
        });

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
                        researchLab.setImageURL(buffer.getText().toString().trim());
                        FirebaseResearchLabHelper helper = new FirebaseResearchLabHelper();
                        helper.updateReseachLab(ResearchLabImageEdit.this, researchLab);
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
}
