package com.team13.campusmitra;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.team13.campusmitra.dataholder.User;

public class GotoAddEditRL extends AppCompatActivity {
    Button add;
    Button view;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_to_check_labs);
        add = findViewById(R.id.click_to_add_rl);
        view = findViewById(R.id.click_to_view_rl);
        user = (User)getIntent().getSerializableExtra("USER");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(GotoAddEditRL.this, AddReasearchLabActivity.class);
                startActivity(intent1);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(GotoAddEditRL.this, ResearchLabsRecyclerView.class);
                intent2.putExtra("UTYPE",""+user.getUserType());
                startActivity(intent2);
            }
        });
    }


}

