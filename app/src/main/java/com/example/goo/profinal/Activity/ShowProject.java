package com.example.goo.profinal.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.goo.profinal.R;

public class ShowProject extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_project);


        Intent intent = getIntent();

        TextView txt_projectName = (TextView)findViewById(R.id.txt_projectName);
        TextView txt_project_intro = (TextView)findViewById(R.id.txt_project_intro);
        TextView txt_project_ability = (TextView)findViewById(R.id.txt_ability);
        TextView txt_development_duration = (TextView)findViewById(R.id.txt_development_duration);
        TextView txt_apply_duration = (TextView)findViewById(R.id.txt_apply_duration);
        TextView txt_people_num = findViewById(R.id.txt_people_num);

        txt_projectName.setText(intent.getStringExtra("name"));
        txt_project_intro.setText(intent.getStringExtra("intro"));
        txt_project_ability.setText(intent.getStringExtra("ability"));
        txt_development_duration.setText(intent.getStringExtra("dev_duration"));
        txt_apply_duration.setText(intent.getStringExtra("apply_duration"));
        txt_people_num.setText(intent.getStringExtra("num"));
    }
}
