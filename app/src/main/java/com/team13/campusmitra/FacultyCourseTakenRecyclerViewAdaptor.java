package com.team13.campusmitra;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class FacultyCourseTakenRecyclerViewAdaptor extends RecyclerView.Adapter<FacultyCourseTakenRecyclerViewAdaptor.ViewHolder> implements Filterable {
    private static final String TAG = "SelectCoursesRecyclerViewAdaptor";

    private List<FacultyCourseTakenModel> mModelList;
    private List<FacultyCourseTakenModel> mModelListFull;
    private Context mContext;

    public FacultyCourseTakenRecyclerViewAdaptor(List<FacultyCourseTakenModel> mModelList,Context mcontext ) {
        this.mModelList = mModelList;
        mModelListFull = new ArrayList<>(mModelList);
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cslayout_listitem,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");
        final FacultyCourseTakenModel model = mModelList.get(position);
        holder.tv1.setText(mModelList.get(position).getCourse().getCourseName());
        holder.tv2.setText(mModelList.get(position).getCourse().getCourseCode());
        holder.view.setBackgroundColor(model.isSelected() ? Color.parseColor("#3FAEA8"): Color.WHITE);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.setSelected(!model.isSelected());
                holder.view.setBackgroundColor(model.isSelected() ? Color.parseColor("#3FAEA8"): Color.WHITE);
            }
        });
    }

    @Override
    public int getItemCount() { return mModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private LinearLayout layout;
        AppCompatTextView tv1;
        AppCompatTextView tv2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            tv1 = itemView.findViewById(R.id.scrv_text1);
            tv2 = itemView.findViewById(R.id.scrv_text2);
            layout = itemView.findViewById(R.id.SCLL1);
        }
    }

    public Filter getFilter(){
        return courseFilter;
    }

    private Filter courseFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<FacultyCourseTakenModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(mModelListFull);
            }
            else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (FacultyCourseTakenModel item : mModelListFull){
                    if (item.getCourse().getCourseCode().toLowerCase().contains(filterPattern) || item.getCourse().getCourseName().toLowerCase().contains(filterPattern)){
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
            mModelList.clear();
            mModelList.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };
}
