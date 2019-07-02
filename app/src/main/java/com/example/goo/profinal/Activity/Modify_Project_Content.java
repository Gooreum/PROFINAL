package com.example.goo.profinal.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goo.profinal.Item.Item_project;
import com.example.goo.profinal.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Goo on 2018-02-10.
 */

public class Modify_Project_Content extends AppCompatActivity {

    private ArrayList<Item_project> array;
    private ArrayAdapter<Item_project> adapter;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    Item_project ip;
    EditText dial_projectName, dial_project_intro, dial_ability, dial_development_duration,people_num;
    TextView dial_apply_duration;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customdial_motify_project);


        final Intent intent = getIntent();

        dial_projectName = (EditText) findViewById(R.id.dial_projectName);
        dial_project_intro = (EditText) findViewById(R.id.dial_project_intro);
        dial_ability = (EditText) findViewById(R.id.dial_ability);
        dial_development_duration = (EditText) findViewById(R.id.dial_development_duration);
        dial_apply_duration = (TextView) findViewById(R.id.dial_apply_duration);
        people_num = findViewById(R.id.people_num);

        dial_projectName.setText(intent.getStringExtra("name"));
        dial_project_intro.setText(intent.getStringExtra("intro"));
        dial_ability.setText(intent.getStringExtra("ability"));
        dial_development_duration.setText(intent.getStringExtra("dev"));
        dial_apply_duration.setText(intent.getStringExtra("apply"));
        people_num.setText(intent.getStringExtra("num"));

        //Adapter_home에서 보내준 position값 받아오기
        final int get_index = intent.getIntExtra("index",1);
// dial_projectName.setText();
        Button btn_confirm = findViewById(R.id.btn_confirm);


        dial_apply_duration.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(Modify_Project_Content.this
                        , android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = year + "년 " + month + "월 " + day + "일";
                dial_apply_duration.setText(date);
            }
        };

        //확인버튼
        btn_confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int index = get_index;
                String name = dial_projectName.getText().toString();
                String intro = dial_project_intro.getText().toString();
                String ability = dial_ability.getText().toString();
                String dev = dial_development_duration.getText().toString();
                String apply = dial_apply_duration.getText().toString();
                String num = people_num.getText().toString();
                String history = intent.getStringExtra("history");
                String nick = intent.getStringExtra("nick");
                String image = intent.getStringExtra("image");

//                Item_project item = new Item_project(name,intro,ability,dev,apply,num);
//                item.setProject_name(name);
//                item.setProject_intro(intro);
//                item.setProject_ability(ability);
//                item.setProject_develop_duration(dev);
//                item.setProject_apply_duration(apply);
//                item.people_num = num;
               // Intent intent = new Intent();
                intent.putExtra("name1", name);
                intent.putExtra("intro1", intro);
                intent.putExtra("ability1", ability);
                intent.putExtra("dev1", dev);
                intent.putExtra("apply1", apply);
                intent.putExtra("num", num);
                intent.putExtra("history", history);
                intent.putExtra("nick", nick);
                intent.putExtra("image", image);

                if (getParent() == null) {
                    setResult(1, intent);
                } else {
                    getParent().setResult(1, intent);
                }
               // setResult(2, intent);
                Toast.makeText(getApplicationContext(), "수정되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
