package com.team13.campusmitra;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.team13.campusmitra.R;



public class Projects_Adapter extends RecyclerView.Adapter<Projects_Adapter.Projects_ViewHolder> {
    private String[] data;
    private static final String TAG = "Research Labs Project Adapter";
    public Projects_Adapter(String[] data){
        this.data = data;
    }
    public class Projects_ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView txt;
        public Projects_ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.item_iv1);
            txt = (TextView) itemView.findViewById(R.id.item_tv1);
        }
    }
    @NonNull
    @Override
    public Projects_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: started");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.project_list_item, parent, false);
        return new Projects_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Projects_ViewHolder holder, int position) {
        String title = data[position];
        holder.txt.setText(title);
    }


    @Override
    public int getItemCount() {
        return data.length;
    }
}
