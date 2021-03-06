package com.team13.campusmitra.adaptors;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.team13.campusmitra.R;
import com.team13.campusmitra.R_Lab;
import com.team13.campusmitra.dataholder.Project;
import com.team13.campusmitra.dataholder.ResearchLab;
import com.team13.campusmitra.dataholder.Room;
import com.team13.campusmitra.firebaseassistant.FirebaseProjectHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseResearchLabHelper;
import com.team13.campusmitra.firebaseassistant.FirebaseRoomHelper;

import java.util.ArrayList;
import java.util.List;

public class ResearchLabsRecyclerViewAdaptor extends RecyclerView.Adapter<ResearchLabsRecyclerViewAdaptor.ViewHolder> {
    private static final String TAG = "LabsRecyclerViewAdaptor";
    private AppCompatActivity activity;
    private ArrayList<Room> items;
    private ArrayList<ResearchLab> itemsFull;
    private ArrayList<ResearchLab> researchLabs;
    boolean rights;
    public ResearchLabsRecyclerViewAdaptor(ArrayList<Room> items, ArrayList<ResearchLab> researchLabs, Context mContext) {
        this.items = items;
        this.researchLabs = researchLabs;
        this.mContext = mContext;
    }

    private Context mContext;
    public  ResearchLabsRecyclerViewAdaptor(ArrayList<ResearchLab> items, Context mContext){
        this.researchLabs = items;
        this.mContext = mContext;
    }
    private String uid;
    public  ResearchLabsRecyclerViewAdaptor(ArrayList<ResearchLab> items, Context mContext,String uid,AppCompatActivity activity ){
        this.researchLabs = items;
        this.activity = activity;
        this.mContext = mContext;
        this.uid = uid;
    }
    public ResearchLabsRecyclerViewAdaptor(ArrayList<Room> items, Context mContext, ArrayList<ResearchLab> researchLabs) {
        this.items = items;
        this.researchLabs = researchLabs;
        itemsFull = new ArrayList<>(researchLabs);
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.researchlab_listitem,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");
        final ResearchLab room = researchLabs.get(position);
        Glide.with(mContext)
                .asBitmap()
                .load(room.getImageURL())
                .placeholder(R.drawable.labs)
                .into(holder.image);
        holder.tv2.setText(room.getResearchLabName());
        holder.tv1.setText(room.getResearchLabNumber());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(R.id.rl_list_item_layout == view.getId()) {
                    Intent intent = new Intent(mContext, R_Lab.class);
                    intent.putExtra("Research Lab",room);
                    intent.putExtra("Come from", "Recylcer view");
                    intent.putExtra("UTYPE",uid);
                    mContext.startActivity(intent);
                    Toast toast = Toast.makeText(mContext, "Opening Lab Information", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        rights = false;
        if(uid!=null && uid.equals("1")){
            rights=true;
        }
        if(rights){
            holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    showEditDialog(room);
                    return false;
                }
            });
        }

    }

    private void showEditDialog(final ResearchLab room){
        final AlertDialog dialog;
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_delete_rlab, null);
        Button cancelBtn = view.findViewById(R.id.dialog_cancel_rl_btn);
        Button deleteBtn = view.findViewById(R.id.dialog_delete_rl_btn);
        alertDialog.setView(view);
        alertDialog.setTitle("Delete Research Lab");
        dialog= alertDialog.create();
        dialog.show();
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(room);
                dialog.cancel();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    public void showDialog(final ResearchLab element) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        dialogBuilder.setTitle("Are you sure want to delete ?");

        dialogBuilder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseResearchLabHelper helper = new FirebaseResearchLabHelper();
                DatabaseReference ref = helper.getReference().child(element.getRoomID());
                ref.removeValue();
                FirebaseRoomHelper helper2 = new FirebaseRoomHelper();
                DatabaseReference ref2 = helper2.getReference().child(element.getRoomID());
                ref2.removeValue();


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

    @Override
    public int getItemCount() {
        return researchLabs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView image;
        TextView tv1, tv2;
        CardView layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.rl_lrv_image);
            tv2 = itemView.findViewById(R.id.rl_item_tv2);
            tv1 = itemView.findViewById(R.id.rl_item_tv1);
            layout = itemView.findViewById((R.id.rl_list_item_layout));
        }
    }

    public Filter getFilter(){
        return courseFilter;
    }

    private Filter courseFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ResearchLab> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(itemsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ResearchLab item : itemsFull) {
                    if (item.getResearchLabName().toLowerCase().contains(filterPattern) ||  item.getRoomID().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            researchLabs.clear();
            researchLabs.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };
}
