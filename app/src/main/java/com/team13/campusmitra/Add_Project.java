package com.team13.campusmitra;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.text.Spanned;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.team13.campusmitra.dataholder.Project;
import com.team13.campusmitra.firebaseassistant.FirebaseCoursesHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseProjectHelper;

import java.util.ArrayList;


public class Add_Project extends AppCompatActivity {
    private LinearLayout member_list;
    EditText proj_name;
    ArrayList<Project> projects;
    RecyclerView project_list;
    EditText proj_desc;
    Button add_btn;
    ArrayList<EditText> members;
    ImageView add_mem;
    Context context;
    int id = 1;
    Dynamic_Add_Projects dap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);
        member_list = (LinearLayout) findViewById(R.id.member_list);
        add_mem = (ImageView) findViewById(R.id.add_mem);
        add_btn = (Button) findViewById(R.id.add_image_btn);
        for(int i = 0; i < id; i++){
            //members.add((EditText) view.findViewWithTag());
        }
        add_mem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                id = id+1;
                dap = new Dynamic_Add_Projects(context, id);
                member_list.addView(dap.addNewMember(getApplicationContext()),1 );
            }
        });
        initComponents();

    }
    private void initComponents(){
        proj_name = findViewById(R.id.text_project_name);
        proj_desc = findViewById(R.id.text_desc);

        projects = new ArrayList<>();
        project_list = (RecyclerView) findViewById(R.id.proj_list);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkEditText(proj_name)&&checkEditText(proj_desc)&&members.size()>0){
                    Project p = new Project();
                    p.setProjectName(proj_name.getText().toString().trim());
                    p.setProjectDescription(proj_desc.getText().toString().trim());
                    for(int i = 0; i < id; i++){

                    }
                    FirebaseProjectHelper helper = new FirebaseProjectHelper();
                    helper.addProject(getApplicationContext(),p);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Enter valid Values!!",Toast.LENGTH_LONG).show();
                }
            }
        });
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
}

