package com.team13.campusmitra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.drm.DrmStore;
import android.net.Uri;
import android.os.Handler;
import android.text.Spanned;

import com.bumptech.glide.Glide;
import com.team13.campusmitra.dataholder.TimeTableElement;
import com.team13.campusmitra.dataholder.User;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.team13.campusmitra.adaptors.Projects_Adapter;
import com.team13.campusmitra.dataholder.Project;
import com.team13.campusmitra.dataholder.ResearchLab;
import com.team13.campusmitra.dataholder.Room;
import com.team13.campusmitra.firebaseassistant.FirebaseResearchLabHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseCoursesHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseProjectHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseTimeTableHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseUserHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class R_Lab extends AppCompatActivity {

    TextView HyperLink;
    TextView mentor;
    TextView labName;
    TextView labNumber;
    Spanned Text;
    Projects_Adapter adaptor;
    Button add_proj;
    Room room;
    boolean isActive;
    ResearchLab researchLab;
    Intent myIntent;
    LinearLayout rl_info;
    ImageView lab_Image;
    ArrayList<String> proffesors_email;
    ArrayList<String> professors;
    ArrayList<Project> projects;
    Context context;
    String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    //ArrayList<Project> projects_present;
    RecyclerView project_list;
    private int new_project = 0;
    private boolean editFlag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_lab);
        context = getApplicationContext();
        myIntent = getIntent();
        String from = (String) myIntent.getStringExtra("Come from");
        String userType = (String) myIntent.getStringExtra("UTYPE");
        editFlag = false;

        if(userType!=null && userType.equals("1")){
            editFlag=true;
        }
        if(from!=null&&from.equals("OCR")){

            room = (Room) myIntent.getSerializableExtra("ROOM");
            researchLab = (ResearchLab) myIntent.getSerializableExtra("RL");
        }
        else
            researchLab = (ResearchLab) myIntent.getSerializableExtra("Research Lab") ;
        HyperLink = findViewById(R.id.r_lab_link);
        mentor = findViewById(R.id.r_lab_mentor);
        labName = findViewById(R.id.r_lab_name);
        labNumber = findViewById(R.id.r_lab_number);
        add_proj = findViewById(R.id.add_proj_btn);
        proffesors_email = researchLab.getMentors();
        rl_info = findViewById(R.id.rl_information);
        lab_Image = findViewById(R.id.r_lab_img);
        professors = new ArrayList<>();
        proffesors_email = new ArrayList<>();

        projects = new ArrayList<>();
        project_list = (RecyclerView) findViewById(R.id.proj_list);
        HyperLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = HyperLink.getText().toString();
                if(uri.length()<6){
                    Toast.makeText(getApplicationContext(),"Invalid URL!!!",Toast.LENGTH_SHORT).show();

                }
                else if(!uri.matches(regex)){
                    Toast.makeText(getApplicationContext(),"Invalid URL!!! Write complete URL",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    //intent.setData(Uri.parse(uri));
                    startActivity(intent);
                }
            }
        });

        loadObject();
        loadProjects();

        add_proj.setVisibility(View.GONE);
        if(editFlag) {
            Toast.makeText(getApplicationContext(),"Long Click on Items to Modify",Toast.LENGTH_SHORT).show();
                add_proj.setVisibility(View.VISIBLE);
                add_proj.setOnClickListener(new View.OnClickListener() {
                    @Override
                    //************************Have to update intent*********************
                    public void onClick(View view) {
                        Intent intent = new Intent(R_Lab.this, Add_Project.class);
                        intent.putExtra("R Lab", researchLab);
                        startActivity(intent);
                        //adaptor.notifyDataSetChanged();
                        //loadProjects();
                    }
                });

            rl_info.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    showEditDialog(researchLab, room);
                    //loadObject();
                    return false;
                }
            });
            lab_Image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Intent intent = new Intent(R_Lab.this, ResearchLabImageEdit.class);
                    intent.putExtra("Reseach Lab", researchLab);
                    startActivity(intent);
                    Glide.with(R_Lab.this)
                            .asBitmap()
                            .load(researchLab.getImageURL())
                            .placeholder(R.drawable.lab_sample)
                            .into(lab_Image);
                    //loadObject();
                    //finish();
                    //showEditImageDialog(current);

                    return false;
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(resultCode==RESULT_OK)
        {
            loadProjects();
        }
    }


    private void loadObject(){
        proffesors_email = researchLab.getMentors();
        //final ArrayList<String> professors = new ArrayList<>();
        Log.d("emails ", String.valueOf(proffesors_email.size()));
        FirebaseUserHelper helper = new FirebaseUserHelper();
        DatabaseReference reference = helper.getReference();
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                professors.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    User u = snapshot.getValue(User.class);
                    if(proffesors_email.contains(u.getUserEmail())){

                        professors.add(u.getUserName());}
                }
                Log.d("proff", String.valueOf(professors.size()));
                String proff = professors.get(0);
                for(int i = 1; i < professors.size(); i++){

                    proff = proff +", "+professors.get(i);}
                mentor.setText(proff);
                labNumber.setText(researchLab.getResearchLabNumber());
                Glide.with(R_Lab.this)
                        .asBitmap()
                        .load(researchLab.getImageURL())
                        .placeholder(R.drawable.lab_sample)
                        .into(lab_Image);

                labName.setText(researchLab.getResearchLabName());
                HyperLink.setText(researchLab.getWebPageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void refresh(int milliseconds){
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.d("run", "kkk");
                loadObject();
                loadProjects();
                isActive = false;
            }
        };
    }
    private void showEditDialog(ResearchLab rl, Room r){
        final AlertDialog dialog;
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(R_Lab.this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_edit_researchlab, null);
        final Spinner actionSpinner = view.findViewById(R.id.dialog_edit_researchlab_spinner);
        final EditText et = view.findViewById(R.id.dialog_edit_researchlab_et);
        Button updateBtn = view.findViewById(R.id.dialog_edit_researchlab_update_btn);
        loadDataInSpinner(actionSpinner,getActions());
        alertDialog.setView(view);
        alertDialog.setTitle("Edit Lab Record");
        dialog= alertDialog.create();
        dialog.show();
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = actionSpinner.getSelectedItemPosition();
                if(checkEditText(et)){
                    String data = et.getText().toString().trim();
                    switch (i){
                        case 1:
                            researchLab.setResearchLabName(data);
                            break;
                        case 2:
                            researchLab.setResearchLabNumber(data);
                            break;
                        case 3:
                            ArrayList<String> ment = new ArrayList<String>();
                            String[] m;
                            m = data.split(",");
                            for(int j = 0; j < m.length; j++)
                                ment.add(m[j]);
                            researchLab.setMentors(ment);
                            break;
                        case 4:
                            String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
                            if(data.length()<6){
                                Toast.makeText(getApplicationContext(),"Invalid URL!!!",Toast.LENGTH_SHORT).show();

                            }
                            else if(!data.matches(regex)){
                                Toast.makeText(getApplicationContext(),"Invalid URL!!! Write complete URL",Toast.LENGTH_SHORT).show();
                            }
                            else
                                researchLab.setWebPageURL(data);
                            break;
                        default:
                            et.setError("Some Error occured");
                            et.requestFocus();
                            actionSpinner.requestFocus();

                    }
                    FirebaseResearchLabHelper helper = new FirebaseResearchLabHelper();
                    helper.updateReseachLab(R_Lab.this, researchLab);
                    loadObject();
                    dialog.cancel();
                }
            }
        });



    }


    private void loadDataInSpinner(Spinner daySpinner, ArrayList<String> data) {

        ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, data);
        areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(areasAdapter);

    }

    private ArrayList<String> getActions(){
        ArrayList<String> arr = new ArrayList<>();
        arr.add("None");
        arr.add("Edit Lab name");
        arr.add("Edit Lab Number");
        arr.add("Edit Mentors");
        arr.add("Edit webLink");
        return arr;
    }

    private void loadProjects(){
        final ArrayList<String> rprojects = researchLab.getProjects();
        FirebaseProjectHelper helper = new FirebaseProjectHelper();
        DatabaseReference reference = helper.getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                projects.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Project project = snapshot.getValue(Project.class);
                    if(rprojects.contains(project.getProjectID()))
                        projects.add(project);
                }
                //Projects_Adapter adapter = new Projects_Adapter(projects);
                //project_list.setAdapter(adapter);
                if (projects.size()>0)
                    loadRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean checkEditText(EditText editText){
        if (editText.getText().toString().trim().length()>0){
            return true;
        }
        else{
            editText.setError("Enter valid value");
            editText.requestFocus();
            return false;
        }
    }
    private void loadRecyclerView(){
        Object[] objects = projects.toArray();
        adaptor = new Projects_Adapter(Arrays.copyOf(objects,objects.length, Project[].class),this, editFlag, researchLab);
        adaptor.notifyDataSetChanged();
        project_list.setHasFixedSize(true);
        project_list.setLayoutManager(new LinearLayoutManager(this));
        loadAdaptorToRecyclerView(project_list,adaptor);
        //recyclerView.setAdapter(adaptor);
    }
    private void loadAdaptorToRecyclerView(RecyclerView recyclerView, Projects_Adapter adaptor){
        Context context = recyclerView.getContext();
        LayoutAnimationController controller = null;
        controller = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_anim_fall_down);
        recyclerView.setAdapter(adaptor);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

}
