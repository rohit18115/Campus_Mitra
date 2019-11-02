package com.team13.campusmitra.adaptors;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.team13.campusmitra.R;
import com.team13.campusmitra.dataholder.Appointment;
import com.team13.campusmitra.dataholder.User;
import com.team13.campusmitra.firebaseassistant.FirebaseAppointmentHelper;

import java.util.ArrayList;

public class ProfAppointmentListAdaptor extends RecyclerView.Adapter<ProfAppointmentListAdaptor.ProfAppointmentViewHolder> {

    Context context;
    private ArrayList<User> users;
    private ArrayList<Appointment> profappointmentsList;
    public ProfAppointmentListAdaptor(Context context, ArrayList<User> users, ArrayList<Appointment> profappointmentsList) {
        this.context = context;
        this.users = users;
        this.profappointmentsList = profappointmentsList;
    }



    @NonNull
    @Override
    public ProfAppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemview = layoutInflater.inflate(R.layout.recyclerview_prof_appointment, parent, false);
        return new ProfAppointmentViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfAppointmentViewHolder holder, final int position) {
        User u1 = getUser(profappointmentsList.get(position).getUserID1());
        User u2 = getUser(profappointmentsList.get(position).getUserID2());
        holder.tvProfappointmentTime.setText(profappointmentsList.get(position).getTime());
        holder.tvProfappointmentDate.setText(profappointmentsList.get(position).getDate());
        holder.tvProfDescription.setText(profappointmentsList.get(position).getDescription());
        holder.tvProfUserId2.setText(u1.getUserEmail());
        holder.tvProfUser2name.setText(u1.getUserFirstName()+" "+u1.getUserLastName());

        if(profappointmentsList.get(position).getAppointmentStatus().equals("active")) {
            holder.ivProfStatus.setImageResource(R.drawable.ic_check_circle_green_a400_48dp);
        }else{
            holder.ivProfStatus.setImageResource(R.drawable.ic_check_circle_red_800_48dp);
        }

        holder.ivProfStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertdialog = new AlertDialog.Builder(context);
                alertdialog.setTitle("Delete Appointment");
                alertdialog.setMessage("Are you sure you waant to delete this appointment?");
                alertdialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference databaseReference= new FirebaseAppointmentHelper().getReference().child(profappointmentsList.get(position).getAppointmentID());
                        databaseReference.removeValue();
                        Toast.makeText(context, "Appointment has been deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                alertdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog dialog = alertdialog.create();
                dialog.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return profappointmentsList.size();
    }

    public class ProfAppointmentViewHolder extends RecyclerView.ViewHolder{
        TextView tvProfUserId2, tvProfappointmentDate, tvProfappointmentTime, tvProfDescription, tvProfUser2name;
        ImageView ivProfStatus;

        public ProfAppointmentViewHolder(@NonNull View itemView) {
            super(itemView);

            tvProfappointmentDate = itemView.findViewById(R.id.prof_appointment_date);
            tvProfUser2name = itemView.findViewById(R.id.userid_one_name);
            tvProfUserId2 = itemView.findViewById(R.id.userid_one);
            tvProfappointmentTime = itemView.findViewById(R.id.prof_appointment_time);
            tvProfDescription = itemView.findViewById(R.id.prof_appointment_description);
            ivProfStatus = itemView.findViewById(R.id.prof_appointment_is_active);

        }
    }

    private User getUser(String uid){
        for(User u:users){
            if(uid.toLowerCase().equals(u.getUserId().toLowerCase())){
                return u;
            }
        }
        return null;
    }
}
