package com.team13.campusmitra.fragments;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.team13.campusmitra.R;
import com.team13.campusmitra.StudentExternalDisplay;
import com.team13.campusmitra.dataholder.User;
import com.team13.campusmitra.firebaseassistant.FirebaseUserHelper;

import de.hdodenhof.circleimageview.CircleImageView;

public class BasicProfileFragment extends Fragment implements View.OnClickListener {

    FloatingActionButton fab;
    Animation rotateForward, rotateBackward;
    CircleImageView image;
    AppCompatTextView name;
    AppCompatTextView uname;
    AppCompatTextView oemail;
    AppCompatTextView dob;
    TextInputEditText ETFName;
    TextInputEditText ETUName;
    TextInputEditText ETUname;
    TextInputEditText ETOEmail;
    ProgressBar pb;

    protected void initComponent(View view) {
        image = view.findViewById(R.id.fbp_profile_image);
        name = view.findViewById(R.id.fbp_profile_name);
        dob = view.findViewById(R.id.fbp_dob);
        uname = view.findViewById(R.id.fbp_user_name);
        oemail = view.findViewById(R.id.fbp_oemail);
        pb = view.findViewById(R.id.fbp_pb);
        fab = view.findViewById(R.id.fbp_fab);
    }

    protected void loadData(View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        final String uid = auth.getCurrentUser().getUid();
        FirebaseUserHelper helper = new FirebaseUserHelper();
        DatabaseReference reference = helper.getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    if(user.getUserId().equals(uid)) {
                        Log.d("lololo", "onDataChange: " + user.getUserLastName());
                        Glide.with(BasicProfileFragment.this)
                                .asBitmap()
                                .load(user.getImageUrl())
                                .placeholder(R.drawable.ic_loading)
                                .into(image);
                        name.setText(user.getUserFirstName() + " " + user.getUserLastName());
                        oemail.setText(user.getUserPersonalMail());
                        dob.setText(user.getDob());
                        uname.setText(user.getUserName());
                    }
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
    private void setUpUIView(){
//        ETName = new TextInputEditText(this);
//        ETEmail = new TextInputEditText(this);
//        ETEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
//        ETRollNumber = new TextInputEditText(this);
//        ETInterests = new TextInputEditText(this);
//        ETDepartment = new TextInputEditText(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic_profile, container, false);

        initComponent(view);
        loadData(view);
        setUpUIView();

        rotateForward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_backward);
        fab.setOnClickListener(this);
        name.setOnClickListener(this);
        image.setOnClickListener(this);
        uname.setOnClickListener(this);
        name.setEnabled(false);
        image.setEnabled(false);

        return view;
    }

    @Override
    public void onClick(View view) {

    }
}
