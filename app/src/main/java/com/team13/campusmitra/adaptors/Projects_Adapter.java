package com.team13.campusmitra.adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.team13.campusmitra.ProjectModel;
import com.team13.campusmitra.R;
import com.team13.campusmitra.dataholder.Project;

import java.util.ArrayList;
import java.util.List;


public class Projects_Adapter extends RecyclerView.Adapter<Projects_Adapter.Projects_ViewHolder> {
    //private String[] data;
    private List<Project> data;

    //private static final String TAG = "Research Labs Project Adapter";
    public Projects_Adapter(List<Project> data){
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
        final Project current = data.get(position);
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
        private void bind(Project current) {
            boolean expanded = current.isExpanded();
            if(expanded)
                push_icon.setImageResource(R.drawable.push_up);
            else
                push_icon.setImageResource(R.drawable.push_down);
            subItem.setVisibility(expanded ? View.VISIBLE : View.GONE);
            txt.setText(current.getProjectName());
            img.setImageResource(R.drawable.project_icon_1);//current.getProjectImageURL());
            sub_part_tv1.setText(current.getProjectDescription());
            ArrayList<String> mem =  current.getMembers();
            String mem_list = "";
            for(int i=0;i< mem.size();i++)
                mem_list = mem_list+", "+mem.get(i);
            sub_part_tv2.setText(mem_list);
        }
    }

}
