package com.team13.campusmitra;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Spanned;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.team13.campusmitra.adaptors.AddCourseRecyclerViewAdaptor;
import com.team13.campusmitra.adaptors.Projects_Adapter;
import com.team13.campusmitra.dataholder.Project;
import com.team13.campusmitra.dataholder.ResearchLab;
import com.team13.campusmitra.firebaseassistant.FirebaseCoursesHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseProjectHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;


public class Add_Project extends AppCompatActivity implements View.OnClickListener {
    TextInputEditText member_list;
    TextInputEditText proj_name;
    ArrayList<Project> projects;
    RecyclerView project_list;
    TextView buffer;
    TextInputEditText proj_desc;
    Button add_btn;
    Projects_Adapter adapter;
    ResearchLab researchLab;
    String members_string;
    ArrayList<String> members;
    private CircleImageView projectImage;
    private static final int PICK_IMAGE=1;
    Uri imageUri;
    ProgressBar progressBar;
    private int userType;
    private String userId;
    private String imageUrl = "";
    Projects_Adapter adaptor;
    FirebaseUser currentUser;
    Project p;
    Intent return_intent;

    Context context;
    private void initComponents(){
        members = new ArrayList<>();
        proj_name = findViewById(R.id.text_project_name);
        proj_desc = findViewById(R.id.text_desc);
        progressBar = findViewById(R.id.proj_add_UPpbar);
        member_list = findViewById(R.id.add_members_list);
        projects = new ArrayList<>();
        buffer = findViewById(R.id.Add_proj_Buffer);
        project_list = (RecyclerView) findViewById(R.id.proj_list);
        p = new Project();

    }
    protected void getProjectObject(){
        if(checkEditText(proj_name)&&checkEditText(proj_desc)){

            p.setProjectName(proj_name.getText().toString().trim());
            p.setProjectDescription(proj_desc.getText().toString().trim());
            p.setProjectImageURL(buffer.getText().toString().trim());
            members_string = member_list.getText().toString().trim();
            String[] m;
            m = members_string.split(",");
            for(int i = 0;i < m.length; i++)
                members.add(m[i].replaceAll("\\s", ""));
            p.setMembers(members);

            FirebaseProjectHelper helper = new FirebaseProjectHelper();
            //helper.addProject(getApplicationContext(),p);
            helper.addProjectInResearchLab(Add_Project.this, p, researchLab);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Enter valid Values!!",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);
        return_intent = getIntent();
        researchLab = (ResearchLab) return_intent.getSerializableExtra("R Lab") ;
        add_btn = findViewById(R.id.add_btn);
        add_btn.setOnClickListener(this);
        projectImage = findViewById(R.id.sample_image);
        projectImage.setOnClickListener(new View.OnClickListener() {
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
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        final StorageReference ProjectImageREf = FirebaseStorage.getInstance().getReference("ProjectImageRef/p"+ts+".jpg");
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



    private boolean checkEditText(EditText et){
        if(et.getText().length()<=1){
            et.requestFocus();
            et.setError("Enter Valid values");
            return false;
        }
        return true;
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
            case R.id.add_btn:
                getProjectObject();
                finish();
        }
    }

}

