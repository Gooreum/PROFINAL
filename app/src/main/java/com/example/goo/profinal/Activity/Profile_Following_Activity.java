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

import com.example.goo.profinal.Adapter.Adapter_following;
import com.example.goo.profinal.Item.Item_following;
import com.example.goo.profinal.Item.Item_member;
import com.example.goo.profinal.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Goo on 2018-01-29.
 */

public class Profile_Following_Activity extends AppCompatActivity {
    ListView list;
 //   private Adapter_member adapter_friend;
    private Adapter_following adapter_following;
    //private ArrayList<Item_member> array_friend;
    private ArrayList<Item_following> array_following;
  //  private ArrayList<Item_project> array_project;
    TextView txt_id, txt_following_count;
    Item_member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_following);

        final SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
        final String id = sp.getString("home", "");

        String image = sp.getString("user_image", "");
        txt_id = (TextView) findViewById(R.id.txt_id);

        txt_id.setText(id);


        member = new Item_member(txt_id.getText().toString());



        loadData();



        list = findViewById(R.id.list_following);

        ImageButton btn_home = (ImageButton) findViewById(R.id.btn_home);
        ImageButton btn_friend = (ImageButton) findViewById(R.id.btn_friend);
        ImageButton btn_profile = (ImageButton) findViewById(R.id.btn_profile);
        ImageButton btn_chat = findViewById(R.id.btn_chat);

        //다른 화면으로 넘어가는 버튼 이벤트
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Profile_Following_Activity.this, RealHome.class);

                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });


        btn_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Profile_Following_Activity.this, Member.class);

                startActivityForResult(intent, 4);
                overridePendingTransition(0, 0);


            }
        });
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile_Following_Activity.this, Profile_Activity.class);


                startActivityForResult(intent, 10);
                overridePendingTransition(0, 0);
            }
        });
        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile_Following_Activity.this, Chatting_list.class);


                startActivityForResult(intent, 10);
                overridePendingTransition(0, 0);
            }
        });


        adapter_following = new Adapter_following(this, R.layout.customlist_friend, array_following);

        adapter_following.notifyDataSetChanged();

        list.setAdapter(adapter_following);
        adapter_following.notifyDataSetChanged();
        Log.i("MY", "----Friend_onCreate----");
    }

    private void loadData() {

        final SharedPreferences sp = getSharedPreferences(txt_id.getText().toString()+"following4", MODE_PRIVATE);
        Gson gson = new Gson();
        String json2 = sp.getString("following", null);
        //추천친구 목록 불러오기
        Type type = new TypeToken<ArrayList<Item_following>>() {
        }.getType();
        array_following = gson.fromJson(json2, type);

        if (array_following == null) {
            array_following = new ArrayList<>();
        }

        System.out.println("리스트 로드 됨");

    }



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

        //saveData();
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
