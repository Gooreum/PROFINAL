package com.example.goo.profinal.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.goo.profinal.Adapter.Adapter_request;
import com.example.goo.profinal.Item.Item_request_member;
import com.example.goo.profinal.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Goo on 2018-03-02.
 */

public class Request extends AppCompatActivity {
    ListView list;
    private Adapter_request adapter_request;
    private ArrayList<Item_request_member> array_request;
    TextView txt_accept_count, txt_people_num;
    Button btn_chat;
    int sum = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_request);
        list = findViewById(R.id.list_request);
        //txt_accept_count = findViewById(R.id.txt_accept_count);
        txt_people_num = findViewById(R.id.txt_people_num);
        btn_chat = findViewById(R.id.btn_chat);


        final SharedPreferences sp100 = getSharedPreferences("user_info", MODE_PRIVATE);
        final String id = sp100.getString("home", "");
        final String image = sp100.getString(id + "image", "");

        Intent intent = getIntent();
        final int index = intent.getIntExtra("position", 0);
        final String content_writer = intent.getStringExtra("nick");

        SharedPreferences ok_preferences = getSharedPreferences(index + "request_count", Context.MODE_PRIVATE);
        int count = ok_preferences.getInt("request_member.accept_count", 0);


        if(!id.equals(content_writer)){
            btn_chat.setVisibility(View.INVISIBLE);
        }



        SharedPreferences sp = getSharedPreferences(index + "모집완료", MODE_PRIVATE);
        btn_chat.setText(sp.getString("모집완료","프로젝트 시작"));
        //프로젝트 시작하기 버튼리스너
        if(btn_chat.getText().toString().equals("프로젝트 시작")) {
            btn_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Request.this);
                    builder.setTitle("확인 메세지");
                    builder.setMessage("프로젝트를 시작하시겠습니까?");
                    builder.setPositiveButton("아니오",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.setNegativeButton("예",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    btn_chat.setText("모집완료");

                                    Intent intent = new Intent(Request.this, Chatting.class);
                                    intent.putExtra("position_from_request", index);
                                    intent.putExtra("nick", id);
                                    intent.putExtra("image", image);
                                    startActivity(intent);


                                }
                            });
                    builder.show();


                }
            });

        }

        loadData();
        Log.i("============================", count + "==============================");
        Log.i("============================인덱스는 : ", index + "==============================");


       // txt_accept_count.setText(count + "");
        //모집인원 표시하기
        txt_people_num.setText(intent.getStringExtra("people_num"));

        //어댑터에 모집인원 수 보내주기용 쉐어드
        SharedPreferences people_num_preferences = getSharedPreferences("people_num", MODE_PRIVATE);
        SharedPreferences.Editor editor = people_num_preferences.edit();
        editor.putString("people_num",intent.getStringExtra("people_num"));
        editor.apply();

        adapter_request = new Adapter_request(this, R.layout.custom_content_request, array_request);
        adapter_request.notifyDataSetChanged();
        list.setAdapter(adapter_request);
        adapter_request.notifyDataSetChanged();
        Log.i("MY", "----Request_onCreate----");
    }


    //arraylist 저장
    private void saveData() {

        Intent intent = getIntent();
        int index = intent.getIntExtra("position", 0);

        SharedPreferences sp = getSharedPreferences(index + "모집완료", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("모집완료",btn_chat.getText().toString());
        // editor.clear();

        editor.apply();


        System.out.println("댓글 저장 됨.");
    }


    //프로젝트 요청 명단 로드해오기
    private void loadData() {

        Intent intent = getIntent();
        int index = intent.getIntExtra("position", 0);

        SharedPreferences sp2 = getSharedPreferences(index + "request11", MODE_PRIVATE);
        Gson gson = new Gson();
        String json2 = sp2.getString("request", null);
        Type type = new TypeToken<ArrayList<Item_request_member>>() {
        }.getType();
        array_request = gson.fromJson(json2, type);
        // System.out.println(json2.toString());

        if (array_request == null) {
            array_request = new ArrayList<>();
        }

        System.out.println("리스트 로드 됨");
    }
    @Override
    protected void onRestart() {
        super.onRestart();

        Log.i("MY", "----Messeage_onRestart----");
    }

    @Override
    protected void onStart() {
        super.onStart();

        // The activity is about to become visible.
        Log.i("MY", "----Messeage_onStart----");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("MY", "----Messeage_onResume----");
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
        // Another activity is taking focus (this activity is about to be "paused").

        Log.i("MY", "----Messeage_onPause----");
    }

    @Override
    protected void onStop() {
        super.onStop();
        // The activity is no longer visible (it is now "stopped")
        Log.i("MY", "----Messeage_onStop----");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // The activity is about to be destroyed.
        Log.i("MY", "----Messeage_onDestroy----");
    }


}
