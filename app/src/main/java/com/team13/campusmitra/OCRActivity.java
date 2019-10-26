package com.team13.campusmitra;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.team13.campusmitra.dataholder.Room;
import com.team13.campusmitra.firebaseassistant.FirebaseRoomHelper;
import com.team13.campusmitra.popupmanager.PopupManager;

import java.util.ArrayList;
import java.util.StringTokenizer;
public class OCRActivity extends AppCompatActivity {
    SurfaceView cameraView;
    TextView textView;
    CameraSource cameraSource;
    PopupManager popupManager;
    boolean flag = false;
    final int RequestCameraPermissionID =1001;
    ArrayList<Room> rooms;

    @Override
    protected void onStart() {
        super.onStart();
        rooms = new ArrayList<>();
        FirebaseRoomHelper helper = new FirebaseRoomHelper();
        DatabaseReference reference = helper.getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rooms.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Room room = snapshot.getValue(Room.class);
                    rooms.add(room);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case RequestCameraPermissionID:{
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    if(ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
                        return;
                    }
                    try {
                        cameraSource.start(cameraView.getHolder());
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);
        cameraView = findViewById(R.id.surface_view);
        textView = findViewById(R.id.text_view);
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if(!textRecognizer.isOperational()){
            Toast.makeText(getApplicationContext(),"DETECTOR DEPENDENCY ARE NOT AVAILABLE",Toast.LENGTH_LONG);
        }
        else{
            cameraSource = new CameraSource.Builder(getApplicationContext(),textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280,1080)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();
            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {
                    try{
                        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(OCRActivity.this,new String[]{Manifest.permission.CAMERA},RequestCameraPermissionID);
                            return;
                        }
                        cameraSource.start(cameraView.getHolder());
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                    cameraSource.stop();

                }
            });
            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {

                    final SparseArray<TextBlock> items=detections.getDetectedItems();
                    if(items.size()!=0){
                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();
                                for(int i =0;i<items.size();i++){
                                    TextBlock item = items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    //stringBuilder.append("\n");
                                }
                                StringTokenizer tokenizer = new StringTokenizer(stringBuilder.toString()," ");
                                String ss="";
                                int i=0;
                                if(popupManager!=null && !popupManager.getDialog().isShowing()){
                                    flag = false;
                                }
                               // boolean flag = false;
                                while(tokenizer.hasMoreElements() && flag==false){
                                    ss = tokenizer.nextToken();
                                    Room r = searchKey(ss);
                                    if(r!=null){
                                        textView.setText("YESSSSSS");
                                        System.out.println(r);
                                        flag=true;
                                        int type = r.getRoomType();
                                        launchRoom(type,r);
                                                                            }
                                }
                               // textView.setText(stringBuilder.toString());
                            }
                        });
                    }
                }
            });
        }
    }
    private Room searchKey(String s){
        Room result = null;

        for(Room room:rooms){
            if(s.toLowerCase().equals(room.getRoomNumber().toLowerCase())){
                return room;
            }
        }
        return  result;
    }
    private void launchRoom(int r,Room room){
        switch (r){
            case 0: break;
            case 1: break;
            case 2: break;
            case 3: break;
            case 4: popupLab(room); break;
            case 5: break;
            default: Toast.makeText(this,"No Match Room found",Toast.LENGTH_LONG).show();
        }
    }
    private void popupLab(Room room){
       popupManager = new PopupManager(this);
        popupManager.setContentView(R.layout.dialog_lab_ocr);
        ImageView roomImage =popupManager.getDialog().findViewById(R.id.dialog_lab_ocr_labimage);
        ImageView cancel = popupManager.getDialog().findViewById(R.id.dialog_lab_ocr_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=false;
                popupManager.dismissPopUp();
            }
        });
        TextView roomNumber = popupManager.getDialog().findViewById(R.id.dialog_lab_ocr_roomnumber);
        roomNumber.setText(room.getRoomNumber());

        TextView roomType = popupManager.getDialog().findViewById(R.id.dialog_lab_ocr_roomtype);
        roomType.setText("LAB");

        TextView roombuilding = popupManager.getDialog().findViewById(R.id.dialog_lab_ocr_roombuilding);
        roombuilding.setText(room.getRoomBuilding());

        TextView systemCount = popupManager.getDialog().findViewById(R.id.dialog_lab_ocr_systemcount);
        systemCount.setText("Total Number of Systems: "+room.getSystemCount());
        popupManager.showPopUp();

        Glide.with(this)
                .asBitmap()
                .load(room.getRoomImageURL())
                .placeholder(R.drawable.ic_loading)
                .into(roomImage);

    }
    private void showRoomDialog(Room room){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater= getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_ocr_room,null);
        alertDialog.setView(view);
        TextView roomnumber = view.findViewById(R.id.ocr_room_no_dialog_tv);
        TextView roomBuilding = view.findViewById(R.id.ocr_room_building_dialog_tv);
        Button btn = view.findViewById(R.id.ocr_room_dialog_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        roomnumber.setText(room.getRoomNumber());
        roomBuilding.setText(room.getRoomBuilding());
        alertDialog.setTitle("Room Information");
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                flag = false;
            }
        });
        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }
}
