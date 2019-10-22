package com.team13.campusmitra.adaptors;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.team13.campusmitra.R;
import com.team13.campusmitra.dataholder.Faculty;
import com.team13.campusmitra.dataholder.Student;
import com.team13.campusmitra.dataholder.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentListDisplayAdaptor extends RecyclerView.Adapter<StudentListDisplayAdaptor.ViewHolder> {
    private static final String TAG = "LabsRecyclerViewAdaptor";

    private ArrayList<User> items;
    private ArrayList<Student> students;
    private Context mContext;

    public StudentListDisplayAdaptor(ArrayList<User> items, ArrayList<Student> students, Context mContext) {
        this.items = items;
        this.students = students;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_display_listitems,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");
        final User user = items.get(position);
        final Student student = students.get(position);
        Glide.with(mContext)
                .asBitmap()
                .load(user.getImageUrl())
                .placeholder(R.drawable.ic_loading)
                .into(holder.image);
        holder.tv1.setText(user.getUserFirstName() + " " + user.getUserLastName());
        holder.tv2.setText("Department");
        holder.tv3.setText(user.getUserEmail());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (R.id.sdl_layout == view.getId()) {
                    Toast toast = Toast.makeText(mContext, "Opening Student Profile", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        AppCompatTextView tv1;
        AppCompatTextView tv2;
        AppCompatTextView tv3;
        CardView layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.sdl_image);
            tv1 = itemView.findViewById(R.id.sdl_name);
            tv2 = itemView.findViewById(R.id.sdl_department);
            tv3 = itemView.findViewById(R.id.sdl_emailid);
            layout = itemView.findViewById((R.id.sdl_layout));
        }
    }
}
