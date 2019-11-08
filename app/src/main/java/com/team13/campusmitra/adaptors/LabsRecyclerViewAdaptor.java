package com.team13.campusmitra.adaptors;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.team13.campusmitra.R;
import com.team13.campusmitra.dataholder.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LabsRecyclerViewAdaptor extends RecyclerView.Adapter<LabsRecyclerViewAdaptor.ViewHolder> {
    private static final String TAG = "LabsRecyclerViewAdaptor";

    private ArrayList<Room> items;
    private ArrayList<Room> itemsFull;
    private Context mContext;

    public LabsRecyclerViewAdaptor(ArrayList<Room> items, Context mContext) {
        this.items = items;
        itemsFull = new ArrayList<>(items);
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.labs_listitems,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");
        final Room room = items.get(position);
        Glide.with(mContext)
                .asBitmap()
                .load(room.getRoomImageURL())
                .placeholder(R.drawable.labs)
                .into(holder.image);
        int cap = getPeople(room);
        if(cap>0.7*room.getCapacity()) {
            Log.d("setcol", "onBindViewHolder: "+getPeople(room));
            holder.tv2.setBackgroundColor(Color.RED);
        }
        else if(cap<0.3*room.getCapacity())
            holder.tv2.setBackgroundColor(Color.GREEN);
        else
            holder.tv2.setBackgroundColor(Color.YELLOW);
        holder.tv1.setText("Lab: "+room.getRoomNumber());
        holder.tv2.setText("Occupancy: "+ cap);
        holder.tv3.setText("Situated at "+room.getRoomBuilding());
        holder.tv4.setText("Capacity: "+ room.getCapacity());
        holder.tv5.setText("System Count: "+ room.getSystemCount());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView image;
        AppCompatTextView tv1;
        AppCompatTextView tv2;
        AppCompatTextView tv3;
        AppCompatTextView tv4;
        AppCompatTextView tv5;
        CardView v1;
        LinearLayout l1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.lrv_image);
            tv1 = itemView.findViewById(R.id.lrv_text1);
            tv2 = itemView.findViewById(R.id.lrv_text2);
            tv4=itemView.findViewById(R.id.lrv_text4);
            tv3 = itemView.findViewById(R.id.lrv_text3);
            tv5=itemView.findViewById(R.id.lrv_text5);
            v1=itemView.findViewById(R.id.list_item_layout);
            l1=itemView.findViewById(R.id.l2);
        }
    }
    public Filter getFilter(){
        return courseFilter;
    }

    private Filter courseFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Room> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(itemsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Room item : itemsFull) {
                    if (item.getLabName().toLowerCase().contains(filterPattern) || item.getRoomBuilding().toLowerCase().contains(filterPattern) || item.getRoomNumber().toLowerCase().contains(filterPattern)) {
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
    public static int getPeople(Room r){
        int n=r.getCapacity();
        int x=0;
        int s;
        Random ran = new Random();
        if (n>0){

            x = ran.nextInt(n);

        }
        else{
            s=r.getSystemCount();
            if(s>0){
                x=ran.nextInt(s);
            }
            else{
                x=ran.nextInt(10);
            }

        }
        return x;
    }
}
