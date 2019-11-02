package com.team13.campusmitra.adaptors;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.team13.campusmitra.ProjectImageEdit;
import com.team13.campusmitra.ProjectModel;
import com.team13.campusmitra.R;
import com.team13.campusmitra.dataholder.Course;
import com.team13.campusmitra.dataholder.Project;
import com.team13.campusmitra.dataholder.User;
import com.team13.campusmitra.firebaseassistant.FirebaseCoursesHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseProjectHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseUserHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class Projects_Adapter extends RecyclerView.Adapter<Projects_Adapter.Projects_ViewHolder> {
    //private String[] data;
    private ArrayList<Project> data;
    private Project[] projectArray;
    private AppCompatActivity activity;
    private Context mContext;
    //private static final String TAG = "Research Labs Project Adapter";
    public Projects_Adapter(ArrayList<Project> data, Context mContext){
        this.data = data;
        this.mContext = mContext;
    }
    public Projects_Adapter(Project[] projectArray, AppCompatActivity activity) {
        this.projectArray = projectArray;
        this.activity = activity;
    }

    @Override
    public Projects_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Log.d(TAG, "onCreateViewHolder: started");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.project_list_item, parent, false);
        return new Projects_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Projects_ViewHolder holder, final int position) {
        final Project current = projectArray[position];//data.get(position);

        Glide.with(activity)
                .asBitmap()
                .load(current.getProjectImageURL())
                .placeholder(R.drawable.project_icon_1)
                .into(holder.img);
        holder.txt.setText(current.getProjectName());
        //img.setImageResource(R.drawable.project_icon_1);
        holder.sub_part_tv1.setText(current.getProjectDescription());
        //boolean expanded;
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            // Get the current state of the item
            @Override
            public void onClick(View v) {
                boolean expanded = current.isExpanded();
                current.setExpanded(!expanded);
                notifyItemChanged(position);
            }
        });
        holder.info_linear.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showEditDialog(current);
                return false;
            }
        });
        holder.image_linear.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(activity.getApplicationContext(), ProjectImageEdit.class);
                intent.putExtra("Project",current);
                activity.startActivity(intent);

                //finish();
                //showEditImageDialog(current);

                return false;
            }
        });
        holder.bind(current);

    }


    @Override
    public int getItemCount() {
        return projectArray.length;//data.size();
    }


    public class Projects_ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img, push_icon;
        public TextView txt;
        public View subItem;
        ArrayList<String> mem;
        ArrayList<String> members;
        public LinearLayout image_linear;
        public LinearLayout info_linear;
        public TextView sub_part_tv1, sub_part_tv2;
        public Projects_ViewHolder(View itemView) {
            super(itemView);
            image_linear = itemView.findViewById(R.id.project_image_linear);
            info_linear = itemView.findViewById(R.id.project_info_linear);
            img = itemView.findViewById(R.id.item_iv1);
            txt = itemView.findViewById(R.id.item_tv1);
            sub_part_tv1 = itemView.findViewById(R.id.sub_item_desc);
            sub_part_tv2 = itemView.findViewById(R.id.sub_item_memb);
            subItem = itemView.findViewById(R.id.sub_item);
            push_icon = itemView.findViewById(R.id.push_icon);
        }
        private void bind(Project current) {
            boolean expanded = current.isExpanded();
            if(expanded)
                push_icon.setImageResource(R.drawable.push_up);
            else
                push_icon.setImageResource(R.drawable.push_down);
            subItem.setVisibility(expanded ? View.VISIBLE : View.GONE);
            //current.getProjectImageURL());

            mem =  current.getMembers();
            members =new ArrayList<>();
            FirebaseUserHelper helper = new FirebaseUserHelper();
            DatabaseReference reference = helper.getReference();
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    members.clear();
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        User u = snapshot.getValue(User.class);
                        if(mem.contains(u.getUserEmail()))
                            members.add(u.getUserName());
                    }
                    if(members.size()>0){
                    String mem_list = members.get(0);
                    for(int i=1;i< members.size();i++)
                        mem_list = mem_list+", "+members.get(i);
                    sub_part_tv2.setText(mem_list);}
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }


    private void showEditDialog(final Project project){
        final AlertDialog dialog;
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_edit_project, null);
        final Spinner actionSpinner = view.findViewById(R.id.dialog_edit_project_spinner);
        final EditText et = view.findViewById(R.id.dialog_edit_project_et);
        Button updateBtn = view.findViewById(R.id.dialog_edit_project_update_btn);
        Button deleteBtn = view.findViewById(R.id.dialog_edit_project_delete_btn);
        loadDataInSpinner(actionSpinner,getActions());
        alertDialog.setView(view);
        alertDialog.setTitle("Edit Project Record");
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
                            project.setProjectName(data);
                            break;
                        case 2:
                            project.setProjectDescription(data);
                            break;
                        case 3:
                            ArrayList<String> members;
                            members = (ArrayList<String>) Arrays.asList(data.split(","));
                            project.setMembers(members);
                            break;
                        default:
                            et.setError("Some Error occured");
                            et.requestFocus();
                            actionSpinner.requestFocus();

                    }
                    FirebaseProjectHelper helper = new FirebaseProjectHelper();
                    helper.updateProject(activity,project);
                    dialog.cancel();
                }
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(project);
                dialog.cancel();
            }
        });


    }
    private ArrayList<String> getActions(){
        ArrayList<String> arr = new ArrayList<>();
        arr.add("None");
        arr.add("Edit Project name");
        arr.add("Edit Project Description");
        arr.add("Edit Project Members");
        return arr;
    }
    private void loadDataInSpinner(Spinner daySpinner, ArrayList<String> data) {

        ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, data);
        areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(areasAdapter);

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
    public void showDialog(final Project element) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        dialogBuilder.setTitle("Are you sure want to delete ?");

        dialogBuilder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseProjectHelper helper = new FirebaseProjectHelper();
                DatabaseReference ref = helper.getReference().child(element.getProjectID());
                ref.removeValue();


            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }

        });

        dialogBuilder.show();
    }

}
