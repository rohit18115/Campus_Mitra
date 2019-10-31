package com.team13.campusmitra.adaptors;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.team13.campusmitra.ProjectModel;
import com.team13.campusmitra.R;
import com.team13.campusmitra.dataholder.Course;
import com.team13.campusmitra.dataholder.Project;
import com.team13.campusmitra.firebaseassistant.FirebaseCoursesHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseProjectHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Projects_Adapter extends RecyclerView.Adapter<Projects_Adapter.Projects_ViewHolder> {
    //private String[] data;
    private ArrayList<Project> data;
    private Project[] projectArray;
    private AppCompatActivity activity;
    //private static final String TAG = "Research Labs Project Adapter";
    public Projects_Adapter(ArrayList<Project> data){
        this.data = data;
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
        holder.bind(current);
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

    }


    @Override
    public int getItemCount() {
        return projectArray.length;//data.size();
    }

    public class Projects_ViewHolder extends RecyclerView.ViewHolder{
        private ImageView img, push_icon;
        private TextView txt;
        private View subItem;
        private TextView sub_part_tv1, sub_part_tv2;
        public Projects_ViewHolder(View itemView) {
            super(itemView);
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
            txt.setText(current.getProjectName());
            img.setImageResource(R.drawable.project_icon_1);//current.getProjectImageURL());
            sub_part_tv1.setText(current.getProjectDescription());
            ArrayList<String> mem =  current.getMembers();
            String mem_list = "";
            for(int i=0;i< mem.size();i++)
                mem_list = mem_list+", "+mem.get(i);
            sub_part_tv2.setText(mem_list);
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
        alertDialog.setTitle("Edit Course Record");
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
                        case 4:
                            //code for image upload
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
        arr.add("Edit Project Image");
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
