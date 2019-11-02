package com.team13.campusmitra;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.team13.campusmitra.dataholder.User;
import com.team13.campusmitra.firebaseassistant.FirebaseUserHelper;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,View.OnClickListener {

    DatePickerDialog datePickerDialog ;
    int Year, Month, Day, Hour, Minute;
    Calendar calendar ;
    private CircleImageView profileImage;
    private static final int PICK_IMAGE=1;
    Uri imageUri;
    Button next;

    private int userType;
    private String userId;
    private String imageUrl = "";

    FirebaseUser currentUser;

    TextInputEditText firstName;
    TextInputEditText lastName;
    TextInputEditText userName;
    TextView buffer;
    TextView dob;
    RadioGroup utype;
    ProgressBar progressBar;
    RadioGroup gen;
    TextInputEditText optEmail;
    Button button_datepicker;

    public void initComponents() {
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        dob = findViewById(R.id.text_datepicker);
        optEmail = findViewById(R.id.opt_email);
        gen = findViewById(R.id.gender_prof);
        userName = findViewById(R.id.userName);
        progressBar = findViewById(R.id.UPpbar);
        utype = findViewById(R.id.UPRBuserType);
        buffer = findViewById(R.id.UPBuffer);
    }

    protected void getUserObject() {

        int selectedId = gen.getCheckedRadioButtonId();
        RadioButton radioButton =  findViewById(selectedId);
        int selectedutypeId = utype.getCheckedRadioButtonId();
        RadioButton utyperadioButton =  findViewById(selectedutypeId);

        String fname = "", lname = "", oemail = "", dobi = "",uname = "",buff = "";

        try {
            fname = firstName.getText().toString();
            lname = lastName.getText().toString();
            oemail = optEmail.getText().toString();
            dobi = dob.getText().toString();
            uname = userName.getText().toString();
            buff = buffer.getText().toString().trim();
        } catch (NullPointerException e) {
            Log.d("lolo", "Null pointer exception: ");
        }
        if(fname.isEmpty()) {
            firstName.setError("First Name Can't be empty", null);
            firstName.requestFocus();
        } else if(lname.isEmpty()) {
            lastName.setError("Last Name Can't be empty", null);
            lastName.requestFocus();
        } else if(dobi.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Please Select Date of Birth",Toast.LENGTH_LONG).show();
            button_datepicker.setFocusable(true);
            button_datepicker.setFocusableInTouchMode(true);
            button_datepicker.requestFocus();
        } else if(!oemail.isEmpty() && (!oemail.contains("@"))) {
            optEmail.setError("Not a valid Email", null);
            optEmail.requestFocus();
        } else {

            User user = new User();
            user.setUserName(uname);
            user.setUserFirstName(fname);
            user.setUserLastName(lname);
            user.setGender(radioButton.getText().toString());
            user.setDob(dobi);
            user.setUserPersonalMail(oemail);
            user.setImageUrl(buff);
            FirebaseAuth auth = FirebaseAuth.getInstance();
            String uid = auth.getCurrentUser().getUid();
            user.setUserId(uid);

            String email = auth.getCurrentUser().getEmail();
            Log.d("Email", "getUserObject: " + email);
            Log.d("User Name", "getUserObject: " + userName.getText().toString());
            user.setUserEmail(email);
            int usetype = 0;
            if (utyperadioButton.getText().toString() == "Faculty") {
                usetype = 1;
            }
            user.setUserType(usetype);


            FirebaseUserHelper helper = new FirebaseUserHelper();
            user.setProfileCompleteCount(user.getProfileCompleteCount() + 1);
            helper.addUser(this, user);

        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        initComponents();
        button_datepicker = (Button) findViewById(R.id.button_datepicker);
        profileImage = (CircleImageView)findViewById(R.id.UPCIV);
        next = findViewById(R.id.UPnext);
        next.setOnClickListener(this);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                progressBar.setVisibility(View.VISIBLE);

                startActivityForResult(Intent.createChooser(gallery,"Seclect Picture"),PICK_IMAGE);
            }
        });
        button_datepicker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                datePickerDialog = DatePickerDialog.newInstance(UserProfile.this, Year, Month, Day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setTitle("Date Picker");


                datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialogInterface) {

                        Toast.makeText(UserProfile.this, "Datepicker Canceled", Toast.LENGTH_SHORT).show();
                    }
                });

                datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
            }
        });

    }
    private String uploadImageToFirebase() {
        final StorageReference ProfileImageREf = FirebaseStorage.getInstance().getReference("ProfileImageRef.jpg");
        String result="";
        if (imageUri != null) {
            ProfileImageREf.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return ProfileImageREf.getDownloadUrl();
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
        return "";
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            imageUri = data.getData();

            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                profileImage.setImageBitmap(bitmap);
                uploadImageToFirebase();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int Year, int Month, int Day) {

        String date = "Date: "+Day+"/"+(Month+1)+"/"+Year;

        Toast.makeText(UserProfile.this, date, Toast.LENGTH_LONG).show();

        TextView text_datepicker = (TextView)findViewById(R.id.text_datepicker);
        text_datepicker.setText(date);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.UPnext:
                getUserObject();

        }
    }

    @Override
    public void onBackPressed () {

        super.onBackPressed();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.logout_action_bar,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId()) {
            case R.id.ab_logout:
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                Intent intent1 = new Intent(getApplicationContext(),SignInSplash.class);
                startActivity(intent1);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}