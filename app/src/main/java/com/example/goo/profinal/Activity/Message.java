package com.example.goo.profinal.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.goo.profinal.Adapter.Adapter_message;
import com.example.goo.profinal.Item.Item_message;
import com.example.goo.profinal.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Goo on 2018-02-23.
 */

public class Message extends AppCompatActivity {

    Button btn_confirm, btn_cancel;
    EditText edit_message;
    ArrayList<Item_message> array_messeage;
    ListView list_messeage;
    Item_message messeage;
    Item_message messeage2;
    private ArrayAdapter<Item_message> adapter;

//    int pos;
//    String pos2;
//    int sum;
//    int hi;
//    String hello;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_message);
        list_messeage = findViewById(R.id.list_message);
        final Intent intent = getIntent();


        //로그인화면으로부터 sharedprefence의 아이디와 닉네임 가지고 오기
        final SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
        final String id = sp.getString("home", "");
        final String image = sp.getString(id + "image", "");

        loadData();

        btn_confirm = findViewById(R.id.btn_confirm);
        btn_cancel = findViewById(R.id.btn_cancel);
        edit_message = findViewById(R.id.edit_message);


        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = edit_message.getText().toString();
                String nick = id;
                String profile_image = image;
                int index = intent.getIntExtra("position", 0);
                int sum2 = intent.getIntExtra("sum", 0);

                int hi = sum2 - index - 1;
                String hello = Integer.toString(hi);
                int count = 0;
                int sum = 0;
                if (edit_message.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "댓글을 작성해주세요.", Toast.LENGTH_SHORT).show();
                } else {


                    messeage = new Item_message(nick, message, profile_image);
                    messeage.nick = nick;
                    messeage.message_content = message;
                    //messeage.image = profile_image;
                    array_messeage.add(messeage);


                    System.out.println(sum + "멧세지 갯수임");
                    adapter.notifyDataSetChanged();
                    sum = array_messeage.size();


                    messeage2 = new Item_message(sum);
                    messeage2.messeage_count2 = sum;


                    //댓글 갯수를 저장하고 보내줌.
                    SharedPreferences sp2 = getSharedPreferences("message_count_plz", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp2.edit();
                    editor.putInt(hello.toString() + hello.toString() + "sss", messeage2.messeage_count2);

                    //댓글 쉐어드 전체 삭제
                    //editor.clear();


                    adapter.notifyDataSetChanged();


                    editor.apply();


                    edit_message.setText("");
                    edit_message.requestFocus();

                    Toast.makeText(getApplicationContext(), "댓글이 작성 되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Message.this, RealHome.class);
                startActivity(intent);
            }
        });


        adapter = new Adapter_message(this, R.layout.custom_content_message, array_messeage);
        adapter.notifyDataSetChanged();
        list_messeage.setAdapter(adapter);
    }

    //arraylist 저장
    private void saveData() {
        Intent intent = getIntent();

        //글 항목 순서를 바꿈. 0부터 시작되는 것을 array.size()-position-1부터 시작시킴
        int pos = intent.getIntExtra("position", 0);
        int sum = intent.getIntExtra("sum", 0);
        int hi = sum - pos - 1;
        String hello = Integer.toString(hi);

        SharedPreferences sp = getSharedPreferences(hello.toString() + hello.toString() +"message", MODE_PRIVATE);
       // SharedPreferences sp = getSharedPreferences("message", MODE_PRIVATE);


        SharedPreferences.Editor editor = sp.edit();

        Gson gson = new Gson();

        String json = gson.toJson(array_messeage);

        //editor.putString(hello.toString() + hello.toString() + "s", json);
        editor.putString( "message", json);
        // editor.clear();
        //메세지 갯수저장하기
        editor.putInt(array_messeage.size() + "message_count", array_messeage.size());
        System.out.println("메세지 수는 : " + array_messeage.size());

        editor.apply();


        System.out.println("댓글 저장 됨.");
    }

    //arraylist 불러오기
    private void loadData() {
        Intent intent = getIntent();
        int pos = intent.getIntExtra("position", 0);
        int sum = intent.getIntExtra("sum", 0);
        int hi = sum - pos - 1;
        String hello = Integer.toString(hi);
        SharedPreferences sp = getSharedPreferences(hello.toString() + hello.toString() +"message", MODE_PRIVATE);



        Gson gson = new Gson();

        //  String json = sp.getString(hello.getBytes().toString() , null);
      //  String json = sp.getString(hello.toString() + hello.toString() + "s", null);
        String json = sp.getString( "message", null);

        Type type = new TypeToken<ArrayList<Item_message>>() {
        }.getType();
//        System.out.println("메세지 수는 : " + array_messeage.size());
        array_messeage = gson.fromJson(json, type);


        if (array_messeage == null) {
            array_messeage = new ArrayList<>();

        }

        System.out.println("댓글 로드 됨");
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
