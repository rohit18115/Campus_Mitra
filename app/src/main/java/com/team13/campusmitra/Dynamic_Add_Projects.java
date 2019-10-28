package com.team13.campusmitra;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;

public class Dynamic_Add_Projects {
    Context context;
    int id;
    public Dynamic_Add_Projects(Context context, int id){
        this.context = context;
        this.id = id;
    }
    public EditText addNewMember(Context context){
        final LayoutParams lparams = new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        final EditText edittext = new EditText(context);
        edittext.setId(id);
        edittext.setTag("proj_mem"+id);
        edittext.setHint("Member Name ");
        edittext.setHintTextColor(Color.parseColor("#006978"));
        edittext.setTextColor(Color.parseColor("#006978"));
        edittext.setTextSize(16);
        return edittext;

    }
}
