package com.example.goo.profinal.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.goo.profinal.Adapter.Adapter_follower;
import com.example.goo.profinal.Item.Item_follower;
import com.example.goo.profinal.Item.Item_member;
import com.example.goo.profinal.Item.Item_project;
import com.example.goo.profinal.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Goo on 2018-03-07.
 */

public class Follower_Friend extends AppCompatActivity {
    ListView list;
    private Adapter_follower adapter_follower;
    private ArrayList<Item_follower> array_follower;
    private ArrayList<Item_project> array_project;
    TextView txt_id, txt_id_2;
    Item_member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_follower);


        Intent intent = getIntent();
        String id = intent.getStringExtra("nick");
        txt_id = (TextView) findViewById(R.id.txt_id);

        txt_id.setText(id);
        member = new Item_member(txt_id.getText().toString());


//        loadData2();



        list = findViewById(R.id.follower);
        loadData();


        // array_friend = new ArrayList<>();

        ImageButton btn_home = (ImageButton) findViewById(R.id.btn_home);
        ImageButton btn_chat = findViewById(R.id.btn_chat);
        ImageButton btn_friend = (ImageButton) findViewById(R.id.btn_friend);
        ImageButton btn_profile = (ImageButton) findViewById(R.id.btn_profile);


        //다른 화면으로 넘어가는 버튼 이벤트
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Follower_Friend.this, RealHome.class);

                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });


        btn_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Follower_Friend.this, Member.class);

                startActivityForResult(intent, 4);
                overridePendingTransition(0, 0);


            }
        });
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Follower_Friend.this, Profile_Activity.class);


                startActivityForResult(intent, 10);
                overridePendingTransition(0, 0);
            }
        });
        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Follower_Friend.this, Chatting_list.class);


                startActivityForResult(intent, 10);
                overridePendingTransition(0, 0);
            }
        });

        adapter_follower = new Adapter_follower(this, R.layout.customlist_friend, array_follower);

        adapter_follower.notifyDataSetChanged();
        list.setAdapter(adapter_follower);

        Log.i("MY", "----Friend_onCreate----");
    }


    private void loadData() {

        final SharedPreferences sp = getSharedPreferences(txt_id.getText().toString()+"follower10", MODE_PRIVATE);
        Gson gson = new Gson();


        String json2 = sp.getString("follower", null);


        //추천친구 목록 불러오기

        Type type = new TypeToken<ArrayList<Item_follower>>() {
        }.getType();
        array_follower = gson.fromJson(json2, type);

        if (array_follower == null) {
            array_follower = new ArrayList<>();
        }

        System.out.println("리스트 로드 됨");

    }

    private void saveData() {

        //SharedPreferences sp = getSharedPreferences(txt_id.getText().toString(), MODE_PRIVATE);
        SharedPreferences sp = getSharedPreferences("memberInfo3", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        //리스트의 객체를 문자열 형식으로 바꿈
        Gson gson = new Gson();

        String json = gson.toJson(array_follower);
        editor.putString(txt_id.getText().toString() + 1, json);
        editor.apply();


        System.out.println("리스트 저장 됨");
        System.out.println(json.toString());
    }
    //arraylist 저장

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.i("MY", "----Friend_onRestart----");
    }

    @Override
    protected void onStart() {
        super.onStart();
        // The activity is about to become visible.
        Log.i("MY", "----Friend_onStart----");
    }

    @Override
    protected void onResume() {
        super.onResume();

        // The activity has become visible (it is now "resumed").
        Log.i("MY", "----Friend_onResume----");
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Another activity is taking focus (this activity is about to be "paused").
        //arraylist 저장

        saveData();
        Log.i("MY", "----Friend_onPause----");
    }

    @Override
    protected void onStop() {
        super.onStop();
        // The activity is no longer visible (it is now "stopped")
        Log.i("MY", "----Friend_onStop----");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // The activity is about to be destroyed.
        Log.i("MY", "----Friend_onDestroy----");
    }
}
