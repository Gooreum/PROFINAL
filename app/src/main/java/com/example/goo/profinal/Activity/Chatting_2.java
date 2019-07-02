package com.example.goo.profinal.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.goo.profinal.Adapter.Adapter_Chatting;
import com.example.goo.profinal.Item.Item_chat;
import com.example.goo.profinal.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Goo on 2018-03-05.
 */

public class Chatting_2 extends AppCompatActivity {
    ArrayList<Item_chat> array_chat;
    Adapter_Chatting adapter_chat;
    TextView txt_chat_people;
    Item_chat chat;
    ListView list;
    EditText edit_chat_content;
    Button btn_send, btn_back;

    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy년MM월dd일 hh시mm분");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_chat);


        Intent intent = getIntent();

        list = findViewById(R.id.list_chat);

        btn_send = findViewById(R.id.btn_send);
        btn_back = findViewById(R.id.btn_back);
        txt_chat_people = findViewById(R.id.txt_chat_people);
        edit_chat_content = findViewById(R.id.edit_chat_content);

        loadData();

        //로그인화면으로부터 sharedprefence의 아이디와 닉네임 가지고 오기
        final SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
        final String id = sp.getString("home", "");
        final String image = sp.getString(id + "image", "");



        txt_chat_people.setText(intent.getStringExtra("nick_collect"));

        adapter_chat = new Adapter_Chatting(this, R.layout.custom_content_chat, array_chat);
        adapter_chat.notifyDataSetChanged();
        list.setAdapter(adapter_chat);

        //채팅창 열었을 때 가장 마지막 채팅 내용으로 포커스
        list.setFocusable(true);
        list.setSelected(true);
        list.setSelection(array_chat.size()-1);
        adapter_chat.notifyDataSetChanged();




        //전송 버튼 리스너
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = edit_chat_content.getText().toString();
                String nick = id;
                String profile_image = image;
                String history = getTime();

                if (edit_chat_content.getText().toString().equals("")) {
                    btn_send.setClickable(false);
                    Toast.makeText(getApplicationContext(), " 작성해주세요.", Toast.LENGTH_SHORT).show();
                    btn_send.setClickable(true);
                } else {
                    //btn_send.setClickable(true);
                    chat = new Item_chat(nick, profile_image, message,history);
                    chat.nick = nick;
                    chat.chat_content = message;
                    chat.history = history;

                    array_chat.add(chat);
                    adapter_chat.notifyDataSetChanged();

                    edit_chat_content.setText("");
                    edit_chat_content.requestFocus();
                }
            }
        });
        Intent intent2 = getIntent();
        final int list_position = intent.getIntExtra("chatlist_index",0);
        //Request에서 받은 포지션값. 이놈을 키값으로 넣어 보자.
        final int index = intent.getIntExtra("position_from_request", 0);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Chatting_2.this, Chatting_list.class);
                intent.putExtra("txt_chat_people", txt_chat_people.getText().toString());
                //intent.putExtra("txt_chat_people", txt_chat_people.getText().toString());
                //intent.putExtra("history", array_chat.lastIndexOf(history));
               // intent.putExtra(index+"history", array_chat.get(array_chat.size()-1).history);
                intent.putExtra("history", array_chat.get(array_chat.size()-1).history);
               // intent.putExtra(index+"message", array_chat.get(array_chat.size()-1).chat_content);
                intent.putExtra("message", array_chat.get(array_chat.size()-1).chat_content);
                intent.putExtra("position_from_request",index);
                intent.putExtra("chatlist_index",list_position);

               setResult(999,intent);
               finish();
            }
        });
    }
    private String getTime() {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
        //return String.valueOf(System.currentTimeMillis());
    }

    private void saveData() {

        SharedPreferences index_preferences = getSharedPreferences("index_preferences", MODE_PRIVATE);


        int index = index_preferences.getInt("index_for_chat2",0);
        //Request에서 받은 포지션값. 이놈을 키값으로 넣어 보자.

        Log.i("==============인덱스는 : ", index+"----------------------");
        SharedPreferences sp = getSharedPreferences(index + "accept_for_chat11", MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();
        //리스트의 객체를 문자열 형식으로 바꿈
        Gson gson = new Gson();

        String json = gson.toJson(array_chat);

        editor.putString("chat3", json);

        // editor.clear();
        editor.apply();

        System.out.println("전체 모집글 내용 : " + json.toString());
        System.out.println("리스트 저장 됨");
    }

    private void loadData() {
        SharedPreferences index_preferences = getSharedPreferences("index_preferences", MODE_PRIVATE);


        int index = index_preferences.getInt("index_for_chat2",0);
        SharedPreferences sp0 = getSharedPreferences(index + "accept_for_chat11", Context.MODE_PRIVATE);
        Log.i("==============인덱스는 : ", index+"----------------------");
        Gson gson = new Gson();
        String json2 = sp0.getString("chat3", null);

        Type type = new TypeToken<ArrayList<Item_chat>>() {
        }.getType();
        array_chat = gson.fromJson(json2, type);
        // System.out.println(json2.toString());
        if (array_chat == null) {
            array_chat = new ArrayList<>();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.i("MY", "----Chatting2_onRestart----");
    }

    @Override
    protected void onStart() {
        super.onStart();
        // The activity is about to become visible.
        Log.i("MY", "----Chatting2_onStart----");
    }

    @Override
    protected void onResume() {
        super.onResume();
       // loadData();
        // The activity has become visible (it is now "resumed").
        Log.i("MY", "----Chatting2_onResume----");
    }

    @Override
    protected void onPause() {
        super.onPause();

        saveData();
        Log.i("MY", "----Chatting2_onPause----");
    }

    @Override
    protected void onStop() {
        super.onStop();
        // The activity is no longer visible (it is now "stopped")
        Log.i("MY", "----Chatting2_onStop----");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // The activity is about to be destroyed.
        Log.i("MY", "----Chatting2_onDestroy----");
    }
}
