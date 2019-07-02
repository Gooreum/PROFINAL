package com.example.goo.profinal.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.goo.profinal.Item.Item_project;
import com.example.goo.profinal.R;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Goo on 2018-02-09.
 * 홈 화면의 게시글 추가버튼은 startActivityForResult를 통해서 이 클래스를 불러온다.
 * 따라서 이 클래스는 추가버튼 이벤트 이후 발생한 값을 전달해주는 역할을 한다.
 */

public class Add_Project_Content extends AppCompatActivity {

    private ArrayList<Item_project> array_project;
    private ArrayAdapter<Item_project> adapter;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    Item_project item;
    Item_project item2;
    EditText dial_projectName, dial_project_intro, dial_ability, dial_development_duration, txt_people_num;
    TextView dial_apply_duration;
    //현재시간 구하기
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    SimpleDateFormat mFormat2 = new SimpleDateFormat("yyyy-MM-dd");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_project_content);

        array_project = new ArrayList<>();

        Intent intent = getIntent();

        dial_projectName = (EditText) findViewById(R.id.dial_projectName);
        dial_project_intro = (EditText) findViewById(R.id.dial_project_intro);
        dial_ability = (EditText) findViewById(R.id.dial_ability);
        dial_development_duration = (EditText) findViewById(R.id.dial_development_duration);
        dial_apply_duration = (TextView) findViewById(R.id.dial_apply_duration);
        txt_people_num = findViewById(R.id.txt_people_num);

        Button btn_confirm = findViewById(R.id.btn_confirm);


        dial_apply_duration.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(Add_Project_Content.this
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
                Intent intent2 = getIntent();
                String name = dial_projectName.getText().toString();
                String intro = dial_project_intro.getText().toString();
                String ability = dial_ability.getText().toString();
                String dev = dial_development_duration.getText().toString();
                String apply = dial_apply_duration.getText().toString();
                String num = txt_people_num.getText().toString();
                String nick = intent2.getStringExtra("nick");
                String history = getTime();
                String profile_image = intent2.getStringExtra("image");
                // item = new Item_project(name, intro, ability, dev, apply, num);
                item2 = new Item_project(name, intro, ability, dev, apply, num, nick, history,profile_image);

                array_project.add(0, item2);

                Intent intent = new Intent();
                intent.putExtra("name", name);
                intent.putExtra("intro", intro);
                intent.putExtra("ability", ability);
                intent.putExtra("dev", dev);
                intent.putExtra("apply", apply);
                intent.putExtra("num", num);
                intent.putExtra("nick", nick);
                intent.putExtra("history", history);
                intent.putExtra("image", profile_image);
                setResult(20, intent);


                finish();
            }
        });

    }

    private void saveData_demo() {

        SharedPreferences sp = getSharedPreferences("project_list", MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();
        //리스트의 객체를 문자열 형식으로 바       Gson gson = new Gson();

        String json = gson.toJson(array_project);

        editor.putString("task demo", json);
        // editor.putString("task list5", json+json);

        editor.apply();

        System.out.println("전체 모집글 내용 : " + json.toString());
        System.out.println("리스트 저장 됨");

    }

    private String getTime() {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
      //  return mFormat2.format(mDate);
        return String.valueOf(System.currentTimeMillis());
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.i("MY", "----Home_onRestart----");
    }

    @Override
    protected void onStart() {
        super.onStart();
        // The activity is about to become visible.
        Log.i("MY", "----Home_onStart----");
    }

    @Override
    protected void onResume() {
        super.onResume();

        // The activity has become visible (it is now "resumed").
        Log.i("MY", "----Home_onResume----");
    }

    @Override
    protected void onPause() {
        super.onPause();


        // saveData_demo();
        // Another activity is taking focus (this activity is about to be "paused").

        Log.i("MY", "----Home_onPause----");
    }

    @Override
    protected void onStop() {
        super.onStop();
        // The activity is no longer visible (it is now "stopped")
        Log.i("MY", "----Home_onStop----");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // The activity is about to be destroyed.
        Log.i("MY", "----Home_onDestroy----");
    }

}