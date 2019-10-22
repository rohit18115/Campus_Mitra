package com.team13.campusmitra.adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.team13.campusmitra.ProjectModel;
import com.team13.campusmitra.R;

import java.util.List;


public class Projects_Adapter extends RecyclerView.Adapter<Projects_Adapter.Projects_ViewHolder> {
    //private String[] data;
    private List<ProjectModel> data;

    //private static final String TAG = "Research Labs Project Adapter";
    public Projects_Adapter(List<ProjectModel> data){
        this.data = data;
    }

    @Override
    public Projects_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Log.d(TAG, "onCreateViewHolder: started");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.project_list_item, parent, false);
        return new Projects_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Projects_ViewHolder holder, final int position) {
        final ProjectModel current = data.get(position);
        holder.bind(current);
        //boolean expanded;
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            // Get the current state of the item
            @Override
            public void onClick(View v) {
                boolean expanded = current.isExpanded();
                current.setExpanded(!expanded);
                notifyItemChanged(position);
            }
        });

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class Projects_ViewHolder extends RecyclerView.ViewHolder{
        private ImageView img, push_icon;
        private TextView txt;
        private View subItem;
        private TextView sub_part_tv1, sub_part_tv2;
        public Projects_ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.item_iv1);
            txt = itemView.findViewById(R.id.item_tv1);
            sub_part_tv1 = itemView.findViewById(R.id.sub_item_desc);
            sub_part_tv2 = itemView.findViewById(R.id.sub_item_memb);
            subItem = itemView.findViewById(R.id.sub_item);
            push_icon = itemView.findViewById(R.id.push_icon);
        }
        private void bind(ProjectModel current) {
            boolean expanded = current.isExpanded();
            if(expanded)
                push_icon.setImageResource(R.drawable.push_up);
            else
                push_icon.setImageResource(R.drawable.push_down);
            subItem.setVisibility(expanded ? View.VISIBLE : View.GONE);
            txt.setText(current.getTitle());
            img.setImageResource(current.getImg());
            sub_part_tv1.setText(current.getDesc());
            sub_part_tv2.setText(current.getLink());
        }
    }

}
