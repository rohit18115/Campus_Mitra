package com.team13.campusmitra;

<<<<<<< HEAD
import androidx.appcompat.app.AppCompatActivity;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Calendar;
=======
import android.content.Intent;
>>>>>>> 61ccccb20aaf2470dc940ac1f481aa1d2b25eb81
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

<<<<<<< HEAD
public class UserProfile extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    DatePickerDialog datePickerDialog ;
    int Year, Month, Day, Hour, Minute;
    Calendar calendar ;
    Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        next = findViewById(R.id.UPnext);
        final Button button_datepicker = (Button) findViewById(R.id.button_datepicker);
        button_datepicker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                datePickerDialog = DatePickerDialog.newInstance(UserProfile.this, Year, Month, Day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setTitle("Date Picker");

=======
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class UserProfile extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
>>>>>>> 61ccccb20aaf2470dc940ac1f481aa1d2b25eb81


<<<<<<< HEAD
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {

                        Toast.makeText(UserProfile.this, "Datepicker Canceled", Toast.LENGTH_SHORT).show();
                    }
                });

                datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
            }
        });
        next.setOnClickListener(this);
    }
    @Override
    public void onDateSet(DatePickerDialog view, int Year, int Month, int Day) {

        String date = "Date: "+Day+"/"+(Month+1)+"/"+Year;

        Toast.makeText(UserProfile.this, date, Toast.LENGTH_LONG).show();

        TextView text_datepicker = (TextView)findViewById(R.id.text_datepicker);
        text_datepicker.setText(date);

    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.UPnext :
                Intent intent1 = new Intent(UserProfile.this, StudentProfile.class);
                startActivity(intent1);
        }
    }
=======
    }
>>>>>>> 61ccccb20aaf2470dc940ac1f481aa1d2b25eb81
}

