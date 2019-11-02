package com.team13.campusmitra.adaptors;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class AppointmentListAdapter extends RecyclerView.Adapter<AppointmentListAdapter.AppointmentViewHolder> {

    Context context;

    private ArrayList<User> users;
    public AppointmentListAdapter(Context context, ArrayList<Appointment> appointmentsList,ArrayList<User> users) {
        this.users = users;
        this.context = context;
        this.appointmentsList = appointmentsList;
    }

    ArrayList<Appointment> appointmentsList;



    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemview = layoutInflater.inflate(R.layout.recyclerview_appointment, parent, false);
        return new AppointmentViewHolder(itemview);

    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, final int position) {

        User u1 = getUser(appointmentsList.get(position).getUserID1());
        User u2 = getUser( appointmentsList.get(position).getUserID2());
        holder.tvappointmentTime.setText(appointmentsList.get(position).getTime());
        holder.tvappointmentDate.setText(appointmentsList.get(position).getDate());
        holder.tvDescription.setText(appointmentsList.get(position).getDescription());
        holder.tvUserId2.setText(u2.getUserEmail());
        holder.tvUser2name.setText(u2.getUserFirstName()+" "+u2.getUserLastName());

        if(appointmentsList.get(position).getAppointmentStatus().equals("active")) {
            holder.ivStatus.setImageResource(R.drawable.ic_check_circle_green_a400_48dp);
        }else{
            holder.ivStatus.setImageResource(R.drawable.ic_check_circle_red_800_48dp);
        }

        holder.ivStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertdialog = new AlertDialog.Builder(context);
                alertdialog.setTitle("Delete Appointment");
                alertdialog.setMessage("Are you sure you waant to delete this appointment?");
                alertdialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference databaseReference= new FirebaseAppointmentHelper().getReference().child(appointmentsList.get(position).getAppointmentID());
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
        return appointmentsList.size();
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserId2, tvappointmentDate, tvappointmentTime, tvDescription, tvUser2name;
        ImageView ivStatus;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);

            tvappointmentDate = itemView.findViewById(R.id.appointment_date);
            tvDescription = itemView.findViewById(R.id.appointment_description);
            tvUserId2 = itemView.findViewById(R.id.userid_two);
            tvappointmentTime = itemView.findViewById(R.id.appointment_time);
            ivStatus = itemView.findViewById(R.id.appointment_is_active);
            tvUser2name = itemView.findViewById(R.id.userid_two_name);
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

    public void showDialog(final Appointment appointment) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle("Are you sure want to delete this appointment ?");

        dialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                DatabaseReference databaseReference= new FirebaseAppointmentHelper().getReference().child(appointment.getAppointmentID());
                databaseReference.removeValue();

            }
        });

        dialogBuilder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*Intent intent= new Intent(activity.getApplicationContext(),EditRoomsDetails.class);
                activity.startActivity(intent);
*/
            }

        });

        dialogBuilder.show();
    }


}
