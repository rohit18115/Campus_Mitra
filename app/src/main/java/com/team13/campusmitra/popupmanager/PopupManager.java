package com.team13.campusmitra.popupmanager;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class PopupManager {
    private Context context;
    private AppCompatActivity activity;
    private Dialog dialog;


    public void setContentView(View view){
        dialog.setContentView(view);
    }
    public  void setContentView(int layoutID){
        dialog.setContentView(layoutID);
    }
    public void showPopUp(){
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.show();
    }
    public void dismissPopUp(){
        dialog.dismiss();
    }
    public void cancelPopUp(){
        dialog.cancel();
    }
    public PopupManager(Context context) {
        this.context = context;
        this.dialog = new Dialog(context);
    }

    public PopupManager(Context context, AppCompatActivity activity) {
        this.context = context;
        this.activity = activity;
        this.dialog = new Dialog(context);
    }

    public PopupManager(Context context, AppCompatActivity activity, Dialog dialog) {
        this.context = context;
        this.activity = activity;
        this.dialog = dialog;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public AppCompatActivity getActivity() {
        return activity;
    }

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }
}
