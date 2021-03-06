package com.team13.campusmitra.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.team13.campusmitra.DatePickerFragment;
import com.team13.campusmitra.R;
import com.team13.campusmitra.dataholder.User;
import com.team13.campusmitra.firebaseassistant.FirebaseUserHelper;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class BasicProfileFragment extends Fragment implements View.OnClickListener {

    FloatingActionButton fab;
    Animation rotateForward, rotateBackward;
    CircleImageView image;
    AppCompatTextView name;
    AppCompatTextView uname;
    AppCompatTextView oemail;
    AppCompatTextView dob;
    TextInputEditText ETFirstName,ETLastName;
    TextInputEditText ETUName;
    TextInputEditText ETOEmail;
    ProgressBar pb;
    String selectedDate;
    Uri imageUri;
    private String imageUrl = "";
    private static final int PICK_IMAGE=1;
    TextView buffer;


    public static final int REQUEST_CODE = 11; // Used to identify the result

    private Context context;
    private String uid;
    private int count = 0;

    public BasicProfileFragment(Context context) {
        // Required empty public constructor
        this.context = context;
    }

    public static BasicProfileFragment newInstance(Context context) {
        BasicProfileFragment fragment = new BasicProfileFragment(context);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    protected void initComponent(View view) {
        image = view.findViewById(R.id.fbp_profile_image);
        name = view.findViewById(R.id.fbp_profile_name);
        dob = view.findViewById(R.id.fbp_dob);
        uname = view.findViewById(R.id.fbp_user_name);
        oemail = view.findViewById(R.id.fbp_oemail);
        pb = view.findViewById(R.id.fbp_pb);
        fab = view.findViewById(R.id.fbp_fab);
        buffer = view.findViewById(R.id.BPFBuffer);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();
    }

    protected void loadData() {
        FirebaseUserHelper helper = new FirebaseUserHelper();
        DatabaseReference reference = helper.getReference().child(uid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user.getUserId().equals(uid)) {
                    Log.d("lololo", "onDataChange: " + user.getUserLastName());
                    String str = user.getGender();
                    Log.d("lololo", "onDataChange: " + user.getImageUrl());
                    if(str.equals("Male")) {
                        Glide.with(context)
                                .asBitmap()
                                .load(user.getImageUrl())
                                .placeholder(R.drawable.blankboy)
                                .into(image);
                        Log.d("lololo", "onDataChange:inside " + user.getImageUrl());
                    }
                    else{
                        Glide.with(context)
                                .asBitmap()
                                .load(user.getImageUrl())
                                .placeholder(R.drawable.blankgirl)
                                .into(image);
                    }
                    name.setText(user.getUserFirstName() + " " + user.getUserLastName());
                    String txt = user.getUserPersonalMail();
                    if(txt!=null && !txt.isEmpty())
                        oemail.setText(txt);
                    dob.setText(user.getDob());
                    String username = user.getUserName();
                    if(username!=null && !username.isEmpty())
                        uname.setText(username);
                    pb.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void uploadData() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        final String uid = auth.getCurrentUser().getUid();
        FirebaseUserHelper helper = new FirebaseUserHelper();
        DatabaseReference reference = helper.getReference().child(uid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                while(getActivity() == null) {
//                    Log.d("crash", "onDataChange: still null ");
//                }
                User user = dataSnapshot.getValue(User.class);
                if(user.getUserId().equals(uid)) {
                    Log.d("upload", "onDataChange: entered upload");
                    String fname = "", lname = "", oEmail = "", dobi = "",uName = "",buff = "";

                    try {
                        String n = name.getText().toString();
                        fname = n.split(" ")[0];
                        lname = n.split(" ")[1];
                        oEmail = oemail.getText().toString();
                        dobi = dob.getText().toString();
                        uName = uname.getText().toString();
                        buff = buffer.getText().toString().trim();
                    } catch (NullPointerException e) {
                        Log.d("lolo", "Null pointer exception: ");
                    }
                    user.setUserName(uName);
                    user.setUserFirstName(fname);
                    user.setUserLastName(lname);
                    user.setDob(dobi);
                    user.setUserPersonalMail(oEmail);
                    if(buff!=null && !buff.isEmpty())
                        user.setImageUrl(buff);
                    new FirebaseUserHelper().addUser(context, user);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void animateFab(){
        fab.startAnimation(rotateForward);
        fab.startAnimation(rotateBackward);
    }

    private void setUpUIView(View view){
        ETFirstName = new TextInputEditText(view.getContext());
        ETLastName = new TextInputEditText(view.getContext());
        ETUName = new TextInputEditText(view.getContext());
        ETOEmail = new TextInputEditText(view.getContext());
        ETOEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic_profile, container, false);

        initComponent(view);
        pb.setVisibility(View.VISIBLE);
        loadData();
        setUpUIView(view);

        rotateForward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_backward);
        fab.setOnClickListener(this);
        name.setOnClickListener(this);
        image.setOnClickListener(this);
        uname.setOnClickListener(this);
        dob.setOnClickListener(this);
        oemail.setOnClickListener(this);
        name.setEnabled(false);
        image.setEnabled(false);
        dob.setEnabled(false);
        uname.setEnabled(false);
        oemail.setEnabled(false);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fbp_fab:
                animateFab();
                count++;
                if(count%2==1) {
                    fab.setImageResource(R.drawable.ic_check);
                    Snackbar.make(view, "Click on each component to edit.", Snackbar.LENGTH_LONG).setTextColor(Color.WHITE)
                            .setAction("Action", null).show();
                    name.setEnabled(true);
                    image.setEnabled(true);
                    uname.setEnabled(true);
                    oemail.setEnabled(true);
                    dob.setEnabled(true);
                } else {
                    fab.setImageResource(R.drawable.ic_mode_edit);
                    name.setEnabled(false);
                    image.setEnabled(false);
                    uname.setEnabled(false);
                    oemail.setEnabled(false);
                    dob.setEnabled(false);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            uploadImageToFirebase();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(context,"No Changes Saved",Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setTitle("Are you Sure you want to save the changes?");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                break;
            case R.id.fbp_profile_name:
                name.setEnabled(true);
                image.setEnabled(true);
                uname.setEnabled(true);
                oemail.setEnabled(true);
                dob.setEnabled(true);
                LinearLayout layout = new LinearLayout(view.getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layout.setLayoutParams(params);
                layout.setOrientation(LinearLayout.VERTICAL);
                ETFirstName = new TextInputEditText(view.getContext());
                ETFirstName.setHint("First Name");
                layout.addView(ETFirstName);
                ETLastName = new TextInputEditText(view.getContext());
                ETLastName.setHint("Last Name");
                layout.addView(ETLastName);
                AlertDialog.Builder builderName = new AlertDialog.Builder(view.getContext());
                builderName.setTitle("Name");
                builderName.setView(ETFirstName);
                builderName.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        name.setText(ETFirstName.getEditableText().toString()+" "+ETLastName.getEditableText().toString());
                    }
                });

                builderName.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                if(ETFirstName.getParent() != null || ETLastName.getParent() != null) {
                    ((ViewGroup)ETFirstName.getParent()).removeView(ETFirstName); // <- fix
                    ((ViewGroup)ETLastName.getParent()).removeView(ETLastName); // <- fix
                }
                builderName.show();

                break;
            case R.id.fbp_oemail:
                name.setEnabled(true);
                image.setEnabled(true);
                uname.setEnabled(true);
                oemail.setEnabled(true);
                dob.setEnabled(true);

                AlertDialog.Builder builderEmail = new AlertDialog.Builder(view.getContext());
                builderEmail.setTitle("Email");
                builderEmail.setView(ETOEmail);
                builderEmail.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = ETOEmail.getEditableText().toString();
                        if(isValidMail(mail)) {
                            oemail.setText(mail);
                        }
                        else {
                            ETOEmail.setError("Not a valid Email", null);
                            ETOEmail.requestFocus();
                            Toast.makeText(context,"Not a Valid Email, Try Again",Toast.LENGTH_SHORT).show();
                        }
                    }
                    private boolean isValidMail(String mail) {
                        if(mail.contains("@"))
                        return true;
                        return false;
                    }
                });

                builderEmail.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                if(ETOEmail.getParent() != null) {
                    ((ViewGroup)ETOEmail.getParent()).removeView(ETOEmail); // <- fix
                }
                builderEmail.show();
                break;
            case R.id.fbp_user_name:
                name.setEnabled(true);
                image.setEnabled(true);
                uname.setEnabled(true);
                oemail.setEnabled(true);
                dob.setEnabled(true);
                AlertDialog.Builder builderUName = new AlertDialog.Builder(view.getContext());
                builderUName.setTitle(" User Name");
                builderUName.setView(ETUName);
                builderUName.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String txt = ETUName.getEditableText().toString();
                        if(txt!=null && !txt.isEmpty()) {
                            uname.setText(txt);
                        }
                    }
                });

                builderUName.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                if(ETUName.getParent() != null) {
                    ((ViewGroup)ETUName.getParent()).removeView(ETUName); // <- fix


                }
                builderUName.show();
                break;
            case R.id.fbp_profile_image:
                Log.d("imagel", "onClick: entered");
                name.setEnabled(true);
                image.setEnabled(true);
                uname.setEnabled(true);
                oemail.setEnabled(true);
                dob.setEnabled(true);
                Log.d("imagel", "onClick: listening");
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                pb.setVisibility(View.VISIBLE);
                startActivityForResult(Intent.createChooser(gallery,"Seclect Picture"),PICK_IMAGE);
                break;
            case R.id.fbp_dob:
                name.setEnabled(true);
                image.setEnabled(true);
                uname.setEnabled(true);
                oemail.setEnabled(true);
                dob.setEnabled(true);
                final FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
                AppCompatDialogFragment newFragment = new DatePickerFragment();
                // set the targetFragment to receive the results, specifying the request code
                newFragment.setTargetFragment(BasicProfileFragment.this, REQUEST_CODE);
                // show the datePicker
                newFragment.show(fm, "datePicker");
                break;
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check for the results
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // get date from string
            selectedDate = data.getStringExtra("selectedDate");
            // set the value of the editText
            dob.setText(selectedDate);
        }
        else if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            imageUri = data.getData();

            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getApplicationContext().getContentResolver(),imageUri);
                image.setImageBitmap(bitmap);
                pb.setVisibility(View.GONE);
                Toast.makeText(context,"Image has been successfully changed",Toast.LENGTH_LONG).show();
                //uploadImageToFirebase();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    private void uploadImageToFirebase() {
        final StorageReference ProfileImageREf = FirebaseStorage.getInstance().getReference("ProfileImageRef/"+uid+".jpg");
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
                        uploadData();
                        Log.d("URL", "onComplete: " + uri.toString());
                    }
                }
            });
        }
        else {
            uploadData();
        }
    }
}
