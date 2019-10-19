package com.team13.campusmitra;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ResearchLabsRecyclerViewAdaptor extends RecyclerView.Adapter<ResearchLabsRecyclerViewAdaptor.ViewHolder> {
    private static final String TAG = "LabsRecyclerViewAdaptor";

    private ArrayList<String> labNumber;
    private ArrayList<String> count;
    private ArrayList<String> address;
    private ArrayList<String> imageUrls;
    private Context mContext;

    public ResearchLabsRecyclerViewAdaptor(ArrayList<String> labNumber, ArrayList<String> count, ArrayList<String> address, ArrayList<String> imageUrls, Context mContext) {
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
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(R.id.list_item_layout == view.getId()) {
                    Toast toast = Toast.makeText(mContext, "Opening Lab Information", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

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
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.lrv_image);
            tv1 = itemView.findViewById(R.id.lrv_text1);
            tv2 = itemView.findViewById(R.id.lrv_text2);
            tv3 = itemView.findViewById(R.id.lrv_text3);
            layout = itemView.findViewById((R.id.list_item_layout));
        }
    }
}
