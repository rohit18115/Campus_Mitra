package com.team13.campusmitra.adaptors;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.team13.campusmitra.R;

import java.util.ArrayList;

public class LabsRecyclerViewAdaptor extends RecyclerView.Adapter<LabsRecyclerViewAdaptor.ViewHolder> {
    private static final String TAG = "LabsRecyclerViewAdaptor";

    private ArrayList<String> labNumber;
    private ArrayList<String> count;
    private ArrayList<String> address;
    private ArrayList<String> imageUrls;
    private Context mContext;

    public LabsRecyclerViewAdaptor(ArrayList<String> labNumber, ArrayList<String> count, ArrayList<String> address, ArrayList<String> imageUrls, Context mContext) {
        this.labNumber = labNumber;
        this.count = count;
        this.address = address;
        this.imageUrls = imageUrls;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitems,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");
        Glide.with(mContext)
                .asBitmap()
                .load(imageUrls.get(position))
                .placeholder(R.drawable.ic_loading)
                .into(holder.image);
        holder.tv1.setText(labNumber.get(position));
        holder.tv2.setText(count.get(position));
        holder.tv3.setText(address.get(position));
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView image;
        AppCompatTextView tv1;
        AppCompatTextView tv2;
        AppCompatTextView tv3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.lrv_image);
            tv1 = itemView.findViewById(R.id.lrv_text1);
            tv2 = itemView.findViewById(R.id.lrv_text2);
            tv3 = itemView.findViewById(R.id.lrv_text3);
        }
    }
}
