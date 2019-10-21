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
import com.team13.campusmitra.dataholder.Room;
import com.team13.campusmitra.dataholder.User;

import java.util.ArrayList;

public class UserListDisplayAdaptor extends RecyclerView.Adapter<UserListDisplayAdaptor.ViewHolder> {
    private static final String TAG = "LabsRecyclerViewAdaptor";

    private ArrayList<User> items;
    private Context mContext;

    public UserListDisplayAdaptor(ArrayList<User> items, Context mContext) {
        this.items = items;
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
        final User user = items.get(position);
//        Glide.with(mContext)
//                .asBitmap()
//                .load(user.getImageUrl())
//                .placeholder(R.drawable.ic_loading)
//                .into(holder.image);
//        holder.tv1.setText("Lab: "+room.getRoomNumber());
//        holder.tv2.setText("Capacity: "+room.getCapacity());
//        holder.tv3.setText("Situated at "+room.getRoomBuilding()+" System Count: "+room.getSystemCount());
//        holder.layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if(R.id.list_item_layout == view.getId()) {
//                    Toast toast = Toast.makeText(mContext, "Opening Lab Information", Toast.LENGTH_SHORT);
//                    toast.show();
//                }
//            }
//        });

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
        CardView layout;

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
