package com.team13.campusmitra.adaptors;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.team13.campusmitra.FacultyExternalDisplayActivity;
import com.team13.campusmitra.R;
import com.team13.campusmitra.StudentExternalDisplay;
import com.team13.campusmitra.dataholder.Faculty;
import com.team13.campusmitra.dataholder.User;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfListDisplayAdaptor extends RecyclerView.Adapter<ProfListDisplayAdaptor.ViewHolder> {
    private static final String TAG = "LabsRecyclerViewAdaptor";

    private ArrayList<User> items,itemsFull;
    private ArrayList<Faculty> profs,profsFull;
    private Context mContext;

    public ProfListDisplayAdaptor(ArrayList<User> items, ArrayList<Faculty> profs, Context mContext) {
        this.items = items;
        this.profs = profs;
        itemsFull = new ArrayList<>(items);
        profsFull = new ArrayList<>(profs);
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.professor_display_listitems,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");
        final User user = items.get(position);
        final Faculty prof = profs.get(position);
        Glide.with(mContext)
                .asBitmap()
                .load(user.getImageUrl())
                .placeholder(R.drawable.ic_loading)
                .into(holder.image);
        holder.tv1.setText(user.getUserFirstName()+ " "+user.getUserLastName());
        holder.tv2.setText(prof.getDepartment()+ " Department");
        holder.tv3.setText(user.getUserEmail());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(R.id.pdl_layout == view.getId()) {
                    Toast toast = Toast.makeText(mContext, "Opening Student Profile", Toast.LENGTH_SHORT);
                    toast.show();
                    Intent newIntent = new Intent(mContext, FacultyExternalDisplayActivity.class);
                    newIntent.putExtra("UserID",user.getUserId());
                    mContext.startActivity(newIntent);
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
            image = itemView.findViewById(R.id.pdl_image);
            tv1 = itemView.findViewById(R.id.pdl_name);
            tv2 = itemView.findViewById(R.id.pdl_department);
            tv3 = itemView.findViewById(R.id.pdl_emailid);
            layout = itemView.findViewById((R.id.pdl_layout));
        }
    }
    public Filter getFilter(){
        return nameFilter;
    }
    private Filter nameFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<User> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(itemsFull);
            }
            else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (User item : itemsFull){
                    if (item.getUserFirstName().toLowerCase().contains(filterPattern) || item.getUserLastName().toLowerCase().contains(filterPattern)){
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
            items.clear();
            items.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };
}

