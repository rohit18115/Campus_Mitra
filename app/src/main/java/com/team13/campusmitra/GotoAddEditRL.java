package com.team13.campusmitra;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class GotoAddEditRL extends AppCompatActivity {
    Button add;
    Button view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_to_check_labs);
        add.findViewById(R.id.click_to_add_rl);
        view.findViewById(R.id.click_to_view_rl);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GotoAddEditRL.this, AddReasearchLabActivity.class);
                startActivity(intent);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GotoAddEditRL.this, ResearchLabsRecyclerView.class);
                startActivity(intent);
            }
        });
    }


}

