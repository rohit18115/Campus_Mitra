package com.team13.campusmitra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spanned;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.os.Bundle;
import android.widget.TextView;

public class R_Lab extends AppCompatActivity {
    TextView HyperLink;
    Spanned Text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_lab);
        HyperLink = (TextView)findViewById(R.id.r_lab_et3);

        Text = Html.fromHtml("Website: " +
                "<a href=' http://iab-rubric.org/'> http://iab-rubric.org/</a>");
        HyperLink.setMovementMethod(LinkMovementMethod.getInstance());
        HyperLink.setText(Text);
        /*RecyclerView project_list;
        project_list = (RecyclerView) findViewById(R.id.proj_list);
        project_list.setLayoutManager(new LinearLayoutManager(this));
        String[] project_items = {"Project 1", "Project 2", "Project 3", "Project 4", "Project 5", "Project 6","Project 7","Project 8","Project 9","Project 10"};
        project_list.setAdapter(new Projects_Adapter(project_items));*/
    }
}
