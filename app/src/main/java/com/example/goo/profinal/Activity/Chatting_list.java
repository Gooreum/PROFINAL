package com.example.goo.profinal.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.goo.profinal.Adapter.Adapter_chat_list;
import com.example.goo.profinal.Item.Item_chat_list;
import com.example.goo.profinal.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Goo on 2018-03-03.
 * 채팅 목록을 불러오는 곳이다.
 */

public class Chatting_list extends AppCompatActivity {
    ArrayList<Item_chat_list> array_chat_list;
    Adapter_chat_list adapter_chat_list;
    ListView list;
    Item_chat_list chat_list;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_chat_list);

        Intent intent = getIntent();
        list = findViewById(R.id.list_chat);
        //홈 레이아웃에 있는 이미지 버튼 객체 생성
        ImageButton btn_home = (ImageButton) findViewById(R.id.btn_home);
        ImageButton btn_friend = (ImageButton) findViewById(R.id.btn_friend);
        ImageButton btn_profile = (ImageButton) findViewById(R.id.btn_profile);
        ImageButton btn_chat = findViewById(R.id.btn_chat);


        final SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
        final String id = sp.getString("home", "");


                loadData();
//            for(int i = 0; i<array_chat_list.size();i++){
//                if(!array_chat_list.get(i).nick.contains(id)){
//                    list.setVisibility(View.INVISIBLE);
//
//                }
//            }

        //Request에서 받은 포지션값. 이놈을 키값으로 넣어 보자.
        final int index = intent.getIntExtra("position_from_request", 0);

        final String nick_collection = intent.getStringExtra(index + "txt_chat_people");
        final String history = intent.getStringExtra(index + "history");
        final String message = intent.getStringExtra(index + "message");


        if (nick_collection != null && history != null && message != null) {
            chat_list = new Item_chat_list(nick_collection, history, message);
            chat_list.nick = nick_collection;
            chat_list.history = history;
            chat_list.chat_content = message;

            array_chat_list.add(0,chat_list);
        }


        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Chatting_list.this, RealHome.class);

                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });


        btn_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Chatting_list.this, Member.class);

                startActivityForResult(intent, 4);
                overridePendingTransition(0, 0);


            }
        });
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Chatting_list.this, Profile_Activity.class);


                startActivityForResult(intent, 10);
                overridePendingTransition(0, 0);
            }
        });
        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Chatting_list.this, Chatting_list.class);


                startActivityForResult(intent, 10);
                overridePendingTransition(0, 0);
            }
        });


        adapter_chat_list = new Adapter_chat_list(this, R.layout.custom_content_chat_list, array_chat_list);
        adapter_chat_list.notifyDataSetChanged();
        list.setAdapter(adapter_chat_list);

        adapter_chat_list.notifyDataSetChanged();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(Chatting_list.this, Chatting_2.class);
            //    Intent intent = new Intent(Chatting_list.this, Chatting.class);
                intent.putExtra("nick_collect", array_chat_list.get(position).nick);
                intent.putExtra("position_from_request", index);
                intent.putExtra("chatlist_index", position);
                startActivityForResult(intent,999);

            }
        });

        Log.i("MY", "----Chat_list onCreate----");
    }

    private void loadData() {

        final SharedPreferences sp2 = getSharedPreferences("user_info", MODE_PRIVATE);
        final String id = sp2.getString("id", "");

        Intent intent = getIntent();
        int index = intent.getIntExtra("position", 0);
        String nick_collection = intent.getStringExtra(index + "txt_chat_people");


            SharedPreferences sp0 = getSharedPreferences("chat_list10", Context.MODE_PRIVATE);

            Gson gson = new Gson();
            String json2 = sp0.getString("chat_list", null);

            Type type = new TypeToken<ArrayList<Item_chat_list>>() {
            }.getType();
            array_chat_list = gson.fromJson(json2, type);
            // System.out.println(json2.toString());
            if (array_chat_list == null) {
                array_chat_list = new ArrayList<>();
            }

    }
    private void saveData() {
        final SharedPreferences sp2 = getSharedPreferences("user_info", MODE_PRIVATE);
        final String id = sp2.getString("home", "");

        Intent intent = getIntent();
        int index = intent.getIntExtra("position", 0);
        String nick_collection = intent.getStringExtra(index + "txt_chat_people");



            SharedPreferences sp3 = getSharedPreferences("chat_list10", MODE_PRIVATE);

            SharedPreferences.Editor editor = sp3.edit();
            //리스트의 객체를 문자열 형식으로 바꿈
            Gson gson = new Gson();

            String json = gson.toJson(array_chat_list);

            editor.putString("chat_list", json);

            //editor.clear();
            editor.apply();




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 999 && resultCode == 999) {

            int index = data.getIntExtra("chatlist_index",0);
            String message = data.getStringExtra("message");
            String history = data.getStringExtra("history");
            String txt_nick_collect = data.getStringExtra("txt_chat_people");
            Item_chat_list chat_list = new Item_chat_list(txt_nick_collect,history,message);
            chat_list.chat_content = message;
            chat_list.nick = txt_nick_collect;
            chat_list.history = history;

            array_chat_list.set(index,chat_list);
            adapter_chat_list.notifyDataSetChanged();
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();

        Log.i("MY", "----Chat_list_onRestart----");
    }

    @Override
    protected void onStart() {
        super.onStart();
        // The activity is about to become visible.
        Log.i("MY", "----Chat_list_onStart----");
    }

    @Override
    protected void onResume() {
        super.onResume();
       // loadData();
        // The activity has become visible (it is now "resumed").
        Log.i("MY", "----Chat_list_onResume----");
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();

        Log.i("MY", "----Chat_list_onPause----");
    }

    @Override
    protected void onStop() {
        super.onStop();
        // The activity is no longer visible (it is now "stopped")
        Log.i("MY", "----Chat_list_onStop----");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // The activity is about to be destroyed.
        Log.i("MY", "----Chat_list_onDestroy----");
    }
}