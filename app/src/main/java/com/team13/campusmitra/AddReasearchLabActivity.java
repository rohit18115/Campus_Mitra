package com.team13.campusmitra;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.team13.campusmitra.dataholder.ResearchLab;
import com.team13.campusmitra.firebaseassistant.FirebaseCoursesHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseResearchLabHelper;

public class AddReasearchLabActivity extends AppCompatActivity {
    TextView room_no;
    TextView room_name;
    TextView room_proff;
    TextView room_build;
    Button add_proj;
    Button done;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reasearch_lab);
            room_no = findViewById(R.id.rooms_number);
            room_name = findViewById(R.id.lab_name);
            room_proff = findViewById(R.id.rooms_professor);
            room_build = findViewById(R.id.rooms_building);
            add_proj = findViewById(R.id.add_proj_btn);
            done = findViewById(R.id.rooms_done);
            done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkTextView(room_no)&&checkTextView(room_name)&&checkTextView(room_proff)&&checkTextView(room_build)){
                    ResearchLab rl = new ResearchLab();
                    rl.setRoomID(room_no.getText().toString().trim());
                    rl.setResearchLabName(room_name.getText().toString().trim());
                    //rl.setMentors();cd
                    FirebaseResearchLabHelper helper = new FirebaseResearchLabHelper();
                    helper.addResearchLab(getApplicationContext(),rl);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Enter valid Values!!",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    private boolean checkTextView(TextView et){
        if(et.getText().length()<=1){
            et.requestFocus();
            et.setError("Enter Valid values");
            return false;
        }
        return true;
    }
}
