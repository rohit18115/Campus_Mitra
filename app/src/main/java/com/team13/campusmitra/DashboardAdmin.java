package com.team13.campusmitra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DashboardAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);
    }
    public void send_to_scan(View view)
    {
        Intent intent = new Intent(getApplicationContext(), OCRActivity.class);

        startActivity(intent);

    }
    public void sendToTimetable(View view)
    {
        Intent intent = new Intent(getApplicationContext(), AddTimeTableActivity.class);

        startActivity(intent);

    }
    public void sendToCourse(View view)
    {
        Intent intent = new Intent(getApplicationContext(), AddCourseActivity.class);

        startActivity(intent);

    }
}
